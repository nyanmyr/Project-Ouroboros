# To-do:
- update diagram
- investigate why component array is dependent of component manager
- make a Game.cpp file and run SFML there
- break up engine into headers again (but with a centralized core).

# Direction Initiative

### Phase 1: Top-Down
- add screen as border.
- revisit movement (choose on discretion if needs reworking)
- move towards something component

### Phase 2: Side Scroller
- sprite component
- gravity
- collision

### Phase 3: Physics
- tile based collision

### Phase 4: UI
- IMGUI SFML

### Phase 5: Lighting
- candle 2d

# Engine Roles
### Main (Launcher)
	- launches game
### Game
	- game related global variables
	- systems flow
	- system implementations
	- component
### Engine
	- entity manager
	- component manager
	- component array

## Specified Roles
### Nacre
	- central hub for engine
	- stores all dependencies and inclusions
### Game
	- imports Nacre to use the engine


# Done:
- make component manager a singleton
- remove initialize
- refactor everything (ECS has become unwieldy and coupling has worsened)
	- component array, component and entity manager are spared from purge (to be redecided)

## Notes:
- directionlessness is the root of all evil!
- if you're reading this don't be discouraged that there's much to do, take one step at a time
