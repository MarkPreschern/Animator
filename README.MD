# Easy Animator
- Displays custom animations in visual, textual, and svg file format using MVC design
  pattern and javax.swing
- Allows for editing, adding, and removing of images with intuitive user interface
## To Use
- Find Animator.jar in resources directory
- From command line, type `java -jar Animator.jar -in <file path> -view <type>`,
  optionally include program arguments detailed below
## Program Arguments
* *Required*
    - `-in <file path>` to display a file. Sample files are provided in samples directory
    - `-view <type>` where types include `text`, `svg`, `visual`, and `edit`
* *Optional*
    - `-speed <positive integer>` where speed is measured in ticks per second
    - `-out <file path>` to save the animation to a file location
