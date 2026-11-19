# HHST

## About
Natively use Husk Homes warps via sign interaction. Focus for ease of warp management and
enhanced support for Geyser/Floodgate controller users.

## Usage

### Sign Setup
```
+-------------------+
|       [hhw]       |
|     warp name     | 
|                   |
|                   |
+-------------------+
         | |
         | |
         | |
```

### Using a Warp
Simply interact with a sign with an empty primary hand. This will trigger the warp listed.

Crouching and interacting will allow editing of the sign

## To Do
- [x] Permissions
  - [x] Create warp signs
  - [x] Edit warp signs (Player)
  - [ ] Edit warp signs (With player create check / others)
  - [x] Remove warp signs (All)
  - [ ] Remove warp signs (With players remove check / others)
  - [ ] Instant sign teleport (4th line bool)
- [x] Support glow and color in signs
  - All warp signs automatically given a color on the [identifier]
- [ ] Save all player warp signs position (mainly debugging/server admin/post-create editing)
- [ ] Support other HH types
  - [ ] Arbitrary X,Y,Z,dimension positions
  - [ ] Homes/PHomes
  - [ ] RTP
  - [ ] Print warp lists (for Geyser/Floodgate users)
  - [ ] Spawn
  - [ ] Back
- [ ] Customization
  - [ ] Warp identifier
  - [ ] Specific sign types only?
- [ ] Check warp on HHST sign creation (config to limit placing or not)