# WorldGenerator
This project will consist of code that will generate blend map, height map and place spawns by random generator for my game.

TODO:
+getDistanceToCampArea(x,y)
River()
+getDistanceToRiver(x,y)
Route()
+getDistanceToRoute(x,y)

Or just use getDistanceTo(x,y) for all types, inheritance could be used if found beneficial for checking in the future.

How to generate highmap, rivers, routes and place bridges:
1.   Generate preliminary height grid, use only point (use distanceToCampArea to make more level ground around camps)
2.   Generate rivers (use ai-like-generation, award path far from camps, path flowing from up to down (height map)
     and lenght, "die" at end of map)
     Adjust heights in height grid where river flow to create a valley-like look (possibly add/adjust points in grid). 
3.   Generate routes, possibly in same maner as rivers in other word ai-like. This time use predefined start and end
     for instance a camp. When crossing river try to cross perpendicular and add mark about bridge at point.
     (is it necessary to adjust height grid for route, investigate)
4.   Generate splines from height grid points

Comment: splines could be used to describe rivers width and path but to be able to use splines for path, start
and end of river must be predefined to make it easier to avoid more than one y per x value that a spline requires.
