'''
############
# Lab 0.04 #
############

Overview
--------
Given the following Sample Code, practice using inheritance 
to create specific child classes for different types of Pokemon.

Create the three child classes below:
1. Water Type
When attacking a fire type, the attack is more effective

When attacking a grass type the effect is less effective

When growl is called print out Splish Splash

2. Fire Type
When attacking a water type, the attack is less effective

When attacking a grass type the effect is more effective

When growl is called print out "Fire Fire"

3. Grass Type
When attacking a water type, the attack is more effective

When attacking a fire type the effect is less effective

When growl is called print out "Cheep Cheep"

##############################################################
# Note: In order to check what type an object is you can use #
# isinstance which takes in an object, a class and returns a #
# Boolean if the object is the type of the inputted class.   #
##############################################################

Example Code
------------
my_pet = Pet()
isinstance(my_pet, Pet) # returns true
isinstance(my_pet, Dog) # returns false
'''

import random
import re

#class declarations
class User():
    def __init__(self, username):
        self.username = username
class Pokemon():
    #__init__ specs all necessary parameters that will vary between objects
    #self points back to the object so you don't have to specify every time
    #can assign attributes arbitrarily, but this makes it easier and improves performance
    def __init__(self, name, hp, ap_bottom, ap_top, owner):
        self.name = name
        self.hp = hp
        self.ap_bottom = ap_bottom
        self.ap_top = ap_top
        self.owner = owner
    def __str__(self):
        return self.name
    def attack(self):
        global atk
        atk = (random.randint(self.ap_bottom, self.ap_top))
        return atk
    def defend(self, enemy, ap):
        self.hp -= ap
        if self.hp < 0:
         self.hp = 0
    def heal(self):
        self.hp += 20
    def stats(self):
        return f"{self.name}, {self.hp} HP, {self.ap_top} AP"
class water(Pokemon):
    def growl(self):
        return "Splish splash"
    def defend(self, enemy, ap):
        if isinstance(enemy, grass):
          ap = ap * 1.5
          self.hp -= ap
          if self.hp < 0:
              self.hp = 0
          print(f"Super effective! {enemy.owner.username}'s {enemy} attacked with {ap} AP!\n")
          print(f"{self.owner.username}'s {self.name} lost {ap} HP, {self.hp} HP remains\n")
        elif isinstance(enemy, fire):
          ap = ap * 0.5
          self.hp -= ap
          if self.hp < 0:
              self.hp = 0
          print(f"Not very effective... {enemy.owner.username}'s {enemy} attacked with {ap} AP!\n")
          print(f"{self.owner.username}'s {self.name} lost {ap} HP, {self.hp} HP remains\n")
        else:
            self.hp -= ap
            if self.hp < 0:
              self.hp = 0
            print(f"{self.owner.username}'s {self.name} lost {ap} HP, {self.hp} HP remains\n")
class fire(Pokemon):
    def growl(self):
        return "Fire fire"
    def defend(self, enemy, ap):
        if isinstance(enemy, water):
          ap = ap * 1.5
          self.hp -= ap
          if self.hp < 0:
              self.hp = 0
          print(f"Super effective! {enemy.owner.username}'s {enemy} attacked with {ap} AP!\n")
          print(f"{self.owner.username}'s {self.name} lost {ap} HP, {self.hp} HP remains\n")
        elif isinstance(enemy, grass):
          ap = ap * 0.5
          self.hp -= ap
          if self.hp < 0:
              self.hp = 0
          print(f"Not very effective... {enemy.owner.username}'s {enemy} attacked with {ap} AP!\n")
          print(f"{self.owner.username}'s {self.name} lost {ap} HP, {self.hp} HP remains\n")
        else:
            self.hp -= ap
            if self.hp < 0:
              self.hp = 0
            print(f"{self.owner.username}'s {self.name} lost {ap} HP, {self.hp} HP remains\n")
class grass(Pokemon):
    def growl(self):
        return "Cheep cheep"
    def defend(self, enemy, ap):
        if isinstance(enemy, fire):
          ap = ap * 1.5
          self.hp -= ap
          if self.hp < 0:
              self.hp = 0
          print(f"Super effective! {enemy.owner.username}'s {enemy} attacked with {ap} AP!\n")
          print(f"{self.owner.username}'s {self.name} lost {ap} HP, {self.hp} HP remains\n")
        elif isinstance(enemy, water):
          ap = ap * 0.5
          self.hp -= ap
          if self.hp < 0:
              self.hp = 0
          print(f"Not very effective... {enemy.owner.username}'s {enemy} attacked with {ap} AP!\n")
          print(f"{self.owner.username}'s {self.name} lost {ap} HP, {self.hp} HP remains\n")
        else:
            self.hp -= ap
            if self.hp < 0:
              self.hp = 0
            print(f"{self.owner.username}'s {self.name} lost {ap} HP, {self.hp} HP remains\n")   

#define pokemon as vars
water1 = water("Squirtle", 60, 10, 40, None)
water2 = water("Psyduck", 70, 10, 40, None)
water3 = water("Polywag", 50, 10, 50, None)
fire1 = fire("Charmander", 25, 10, 70, None)
fire2 = fire("Ninetails", 30, 10, 50, None)
fire3 = fire("Ponyta", 40, 10, 60, None)
grass1 = grass("Bulbasaur", 60, 10, 40, None)
grass2 = grass("Bellsprout", 40, 10, 60, None)
grass3 = grass("Oddish", 50, 10, 50, None)

#pokemon list
pokelist = [
    water1,
    water2,
    water3,
    fire1,
    fire2,
    fire3,
    grass1,
    grass2,
    grass3
]

#FUNCTION DEFINITIONS
def pokepick():
    global pokemonselect
    global pokeinventory
    i = 0
    pokeinventory = [0,1,2]
    while i < 3:
      pokemonselect = input()
      #regex to check if input is not a number
      returnsearch = re.search('[1-9]', pokemonselect)
      #this is the only way I found that wouldn't go into else every time
      if returnsearch != None: 
         pokemonselect = int(pokemonselect)
         pokemonselect -= 1 
         pokemonselect = pokelist[pokemonselect]
         pokeinventory[i] = pokemonselect
         pokeinventory[i].owner = char
         i += 1
      else:
          print("Input a valid number (1-9)")
def enemypick():
    #global declarations annoying -_-
    global enemypokemon
    global enemyinventory
    i = 0
    enemyinventory = [0,1,2]
    listlength = len(pokelist) - 1
    while i < 3:
        picknum = random.randint(0,listlength)
        enemypokemon = pokelist[picknum]
        #check if it's already taken by the player
        if enemypokemon.owner != None:
            enemypick()
        else: 
         enemyinventory[i] = enemypokemon
         enemyinventory[i].owner = comp
         i += 1
def pokeselect():
    global pokemonselect
    print("Select your next pokemon:")
    poke1 = pokeinventory[0]
    poke2 = pokeinventory[1]
    poke3 = pokeinventory[2]
    print(poke1.name, poke1.hp, poke1.ap_top)
    print(poke2.name, poke2.hp, poke2.ap_top)
    print(poke3.name, poke3.hp, poke3.ap_top)
    switchput = input()
    returnsearch = re.search('[1-3]', switchput)
    if returnsearch != None:
         switchput = int(switchput)
         switchput = switchput - 1
         pokemonselect = pokeinventory[switchput]
         gameloop()
    else:
        print("Please enter 1-3 to select your pokemon")
        pokeselect()
def gameloop():
    global pokeinventory
    global pokemonselect
    pokehealth = (pokeinventory[0].hp + pokeinventory[1].hp + pokeinventory[2].hp)
    enemyhealth = (enemyinventory[0].hp + enemyinventory[1].hp + enemyinventory[2].hp)
    if pokemonselect.hp <= 0:
            pokeselect()
    if pokehealth > 0:
        if enemyhealth > 0:
            print("[a]ttack, [h]eal, or [s]witch: ")
            playeraction = input()
            if playeraction == "a":
                pokemonselect.attack()
                enemypokemon.defend(pokemonselect, atk)
                enemyaction()
            elif playeraction == "h":
                pokemonselect.heal()
                print(char.username, "'s ", pokemonselect.name, "'s HP is now: ", pokemonselect.hp)
                enemyaction()
            elif playeraction == "s":
                pokeselect()
            else:
                print("Enter a valid command")
                gameloop()
        else:
            print("You win!")
            exit()
    else:
        print("All your pokemon have fainted! You lose...")
        exit()
def enemyaction():
    #have to do this or else python throws a fit
    global enemypokemon
    action = random.randint(0,4)
    if enemypokemon.hp <= 0:
        action = 3
    if action == 0 or 1 or 4:
        enemypokemon.attack()
        pokemonselect.defend(enemypokemon, atk)
        if pokemonselect.hp <= 0:
            pokeselect()
    elif action == 2:
        enemypokemon.heal()
        print(comp.username, "'s ", enemypokemon.name, " healed for 20 HP!")
    else:
        switchnum = random.randint(0,2)
        enemypokemon = enemyinventory[switchnum]
        if enemypokemon.hp <= 0:
            enemyaction()
        else:
            print(comp.username, " switched to ", enemypokemon.name)
    gameloop()

#create user and computer
print("Enter your name: ")
char = User(input())
comp = User("Computer")

#list pokemon and select
print("Select 3 pokemon (1-9): ")
i = 0
#have to use while loop because for loop goes crazy
while i < len(pokelist):
    current = pokelist[i]
    print(current.name)
    print(current.hp, " HP") 
    print(current.ap_top, " AP")
    i += 1

#call pokepick func
pokepick()

#call enemypick
enemypick()

print(f"Your enemy has chosen {enemypokemon.name}, with {enemypokemon.hp} HP\n")
  
#gameloop function call
gameloop()










'''
    #def __str__(self):
        return f"Your name is {self.username}. Your pokemon are {self.inventory}"
    #def newturn(self):
        print(self.inventory)
        #self.activepokemon = input()
    #def userattack(self):
        print("Select attack target: ")
        i = input()
        atk = self.activepokemon.attack()
        i.defend(self.activepokemon, atk)
    #def userheal(self):
        print("Select pokemon to heal: ", self.inventory)
        i = input()
        i.heal()
        return (f"{input.name} was healed for 20 HP!")
    #def userswitch(self):
        print(f"Your pokemon are: {self.inventory}\nSelect your pokemon: ")
        self.activepokemon = input()
        if self.activepokemon == self.inventory:
            self.activepokemon == self.activepokemon
        else:
            print("Please select a pokemon in your inventory")
            '''
'''
def main():
    print("Enter your name: ")
    user = input()
    print("Select your pokemon: \n", pokelist)
    pokinv = input()
    #switch statement to check if correct pokemon has been selected
    char = User(user, pokinv)
    #leftover = 
    #statement to determine leftover goes here
    #comp1 = "Computer"
    #comp = User(comp1, leftover)
    def battle():
        #ask here
        print("Choose your pokemon: ")
        char.newturn()
        pickedpokemon = input()
        if pickedpokemon == char.inventory:
          char.activepokemon = pickedpokemon
        else:
          print("Please select a pokemon")
        print("Select your action (attack, heal, or switch): ")
        action = input()
        if action == "attack":
            char.userattack()
        elif action == "heal":
            char.userheal
        elif action == "switch":
            char.userswitch
        else:
            print("Please select a valid action (attack, heal, switch)")
    return 0
'''
'''
print("Enter your name: ")
user = input()
print("Select your pokemon: \n", pokelist)
pokinv = input()
if pokinv == pokelist[0]:
    print("You have selected ", pokelist[0])
elif pokinv == pokelist[1]:
    print("You have selected ", pokelist[1])
elif pokinv == pokelist[2]:
    print("You have selected ", pokelist[2])
else:
    print("Please select a valid pokemon")
char = User(user, pokinv)
    #leftover = 
    #statement to determine leftover goes here
    #comp1 = "Computer"
    #comp = User(comp1, leftover)
print("Choose your pokemon: ")
char.newturn()
pickedpokemon = input()
if pickedpokemon == char.inventory:
    char.activepokemon = pickedpokemon
else:
    #print("Please select a pokemon")
    print("Select your action (attack, heal, or switch): ")
    action = input()
    if action == "attack":
        char.userattack()
    elif action == "heal":
        char.userheal
    elif action == "switch":
        char.userswitch
    else:
        print("Please select a valid action (attack, heal, switch)")
'''
#print(pokelist)
'''
USED TESTING CODE

print(test)
print(test2)
print(test3)

asdf = water("doug", 10, 10, 10)
print(asdf)

#all_test = [test1, test2, test3]

#for test in all_test:

pwr = test1.attack()
test3.defend(test1, pwr)
test2.defend(test1, pwr)
test1.defend(test1, pwr)

pwr = test2.attack()
test3.defend(test2, pwr)
test2.defend(test2, pwr)
test1.defend(test2, pwr)

pwr = test3.attack()
test3.defend(test3, pwr)
test2.defend(test3, pwr)
test1.defend(test3, pwr)

'''

#messy hardcode, fix later
'''
    if pokemonselect == '1':
        pokemonselect = water1
    elif pokemonselect == '2':
        pokemonselect = water2
    elif pokemonselect == '3':
        pokemonselect = water3
    elif pokemonselect == '4':
        pokemonselect = fire1
    elif pokemonselect == '5':
        pokemonselect = fire2
    elif pokemonselect == '6':
        pokemonselect = fire3
    elif pokemonselect == '7':
        pokemonselect = grass1
    elif pokemonselect == '8':
        pokemonselect = grass2
    elif pokemonselect == '9':
        pokemonselect = grass3
    else:
        print("Select a pokemon using the numbers 1-9")
        pokepick()
'''
'''
if pokemonselect.hp > 0:
    if enemypokemon.hp > 0:
      print("[a]ttack, [h]eal, or [s]witch: ")
      playeraction = input()
      if playeraction == "a":
         pokemonselect.attack()
         #not returning atk because...reasons
         enemypokemon.defend(pokemonselect, atk)
         print(pokemonselect.name, " did ", atk, " damage!\n")
         enemyaction()
      elif playeraction == "h":
         pokemonselect.heal()
         print(pokemonselect.hp)
         enemyaction()
      elif playeraction == "s":
         pokeselect()
      else:
         print("Enter a valid command")
    else:
        print("You win!")
else:
    print("You lose...")
    '''