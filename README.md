# InfiniteRecharge
Project for the 2020 FRC Game - Infinite Recharge

This is an untested implementation of Subsystem-Based Coding

Subsystem based coding works around defining subsystems which have control over ALL their functions, meaning all their functions are defined within the subsystem with Command getters.

This is very good for multiple reasons:
-   Less interdependant classes, which defeat the entire purpose of Object Oriented programming
-   Easier, and simpler readablilty
    Everything is in one location, and on average the amount of coding needed to be done can be cut down into half the lines or less. To understand how the functions work, a coder does not need to jump between RobotContainer, The Subsystem, the command class. Everything is in one space. This also forces the coder to avoid interdependancy between subsystems, which is a good thing. Subsystems and their functions should never have access to other classes' functions. There are special cases in which the Concensus class can be used
-   Less classes in general, and less lines
    the logic is central to the getter and as a result easier to understand what is going on and what the error is. ContinuousCommand offers alot of options to very easily and efficiently impliment very complex logic
-   Gets rid of global RobotContainer Subsystem and Command getters
    Having global getters completely defeats the purpose of information hiding and object oriented programming, and it unnecessarily clogs up RobotContainer with pointless methods

In fact this framework is more "Command-based" than our current programming. Right now we have many functions in place as void methods that we call on a button press or release. Now they are all commands in the subsystems, which take up the same amount of lines in implementation, while also making the functions that buttons complete more obvious

This gets rid of global getters, as there is 0 need for them.
This also more blatently shows the relationship between functions and their respective subsystems, while massively cutting down on the amount of coding we have to do. I got rid of pretty much all of the command classes, while implementing each in around 10 lines in their respective subsystem.

There are certain functions where one subsystem should limit another's capability, Ie: when we climb, we want to have the acquirer deploy. This action is completed by defining static Concensus instances in the subsystem. This class is fairly simplistic and it allows other classes to give a 'vote' as to whether a function should actually be activated, though the subsystem that contains the Concencus object decides what to do with it. In effect, an implemented example is this:

The Climber system does not directly set the acquire to extend,
It adds a 'vote' to the acquirer's shouldExtend concensus object.
If the acquirer senses that the global concensus is that it should not extend, it will not. Even though it will store what it 'wants' to do in the acquirerSpeed primative. This gets rid of any errors with messing up togglers by overriding the state and other logistical issues that come about by subsystems directly running functions of others.

Take a look at the continuous command too. Its pretty cool

Also, MethodCommand is basically the same thing as InstantCommand. It is not worse, or "sketchier". Instead of running the Runnable when the command is scheduled, it runs it once in execute. This is effectively the same, except it works in a few more use cases. Also it has the runOnEnd function that along with First's perpetually decorater allows the command to run the method repeatedly, and do another thing when its canceled. Ie: the whileHeld method on a Button will run the command while held, and cancel it when let go. This means you can use a MethodCommand to bind the spinning of a motor to a button, instead of giving it two void methods with whenPressed and whenReleased.

Right now indexing of the drum array is not implemented. In reality the only reason we have it is for detecting when we have 5 balls, and showing it on Shuffleboard, though its not difficult to implement.

ShooterMotor has been moved into ShooterSubsystem because Motors should not effectively be singletons, and the Lidar has lost its 'Subsystem' status because it is a sensor, not a subsystem. It has no actions. I put it in the Sensor's folder, and it an instance of it is owned by the ShooterMotor subsystem. Subsystems own sensors and corrosponding commands, they should not have access to other subsystems.

This leaves the only question being Auto. Auto is the only thing that we really need Command files for (minus the utility commands Method, Continuous, and Unstoppable Command)

AutoCommands get passed the subsystems they need to use, and they are owned by RobotContainer, as is everything else. From there they can put together functions from the respective subsystems to achieve a nice auto.

So this leaves conventions:
-   Subsystems should not contain any public void methods, only Commands
-   Subsystems should not have access to any other Subsystems
-   Sensors should not be Subsystems (Vision, Lidar, Color sensors, limit switches, PigeonIMU, etc.)
-   Command getters should be prefexed with "cmd" so the programmer can type the subsystem and a period and all the functions of that subsystem will pop up
-   camelCase variables and methods, capital Classes.
-   Only re-usable code should be located in the util folder. (I'm gonna move BallArray somewhere else)
-   Not really against this but, why do we prefix every instance variable "m_"? if everything has it, its effectively pointless. If we want to do this we should have multiple prefixes corrosponding to different things, like motors, sensors, values, etc.

Each team can create code on their own terms. First are not programming gods. Often times in their documentation they recommend that 'advanced teams should look into better options' its doesn't apply to everything they make, but it shows that they encourage teams to branch out from their example. I believe that Subsystem-Based programming is objectively better than the basic precedent they have laid out. The efficiency gained by writing gained by this is astronomical. When the Symons were gone. The robot indexing was completely changed as a result of the shooter being moved to the back of the bot. This format allowed me to only change like 30-50 lines of code to get it running with the brand new logic within half-an-hour of the mechanical change being anounced. It also worked first try. (It was by no means perfect, I spent another 20 minutes fixing issues by running it a second time lol) Either way it was extremely quick, and easy to read, understand, and develop, compared to searching through 3-4 interdependant classes and trying to fix errors that are caused by their reliance on each other. It is also easier to bring new programmers up to speed as all the tools are right there. They do not need to know how to create a class, create the respective getters and setters in RobotContainer, and implement it correctly in the original subsystem. They only need to know how to directly implement the logic.

I would definately like feedback on how to further improve this system, so take a look through the code and tell me what could be improved. Or show me the advantages other frameworks have over this one.