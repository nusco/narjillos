## Contribute to Narjillos

### Installation on Mac
* Download and install the [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* Install [Gradle](https://gradle.org/) via Homebrew `brew install gradle`

### Installation on Linux (Ubuntu based distro)

##### Install Java JDK 8
* Add the required Java JDK 8 Repository `sudo add-apt-repository ppa:webupd8team/java`
* Update the repositories `sudo apt-get update`
* Install [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) `sudo apt-get install oracle-java8-installer`
* Configure Java environment `sudo apt-get install oracle-java8-set-default`

##### Install Gradle
* Create a location to download .zip file `mkdir -p ~/opt/packages/gradle && cd $_`
* Download the .zip file `wget https://services.gradle.org/distributions/gradle-2.3-bin.zip`
* Unzip the file `unzip gradle-2.3-bin.zip`
* Create a symlink which will allow us update Gradle easier later `ln -s ~/opt/packages/gradle-2.3/ ~/opt/gradle`
* Open a text editor and edit (gedit as an example) `gedit ~/.profile`
* Add the following to the bottom of your `.profile` file
```
# Gradle
if [ -d "$HOME/opt/gradle" ]; then
    export GRADLE_HOME="$HOME/opt/gradle"
    PATH="$PATH:$GRADLE_HOME/bin"
fi
```
* Source your file `source ~/.profile`
* Verify Gradle is install `gradle -version`

### Setup the environment

* Fork the [repository](https://github.com/nusco/narjillos/#fork-destination-box)
* Clone the repository to your local environment `git clone [your forked repo]`
* Generate some project files with Eclipse `gradle eclipse` or IntelliJ Idea `gradle idea`
* Run the tests `gradle test`
* Run Narjillos `gradle narjillos`

### Start contributing and further information

* Look at the current backlog `gradle backlog` or `gradle bl` or `g bl`
* See a few additional tasks with `gradle tasks`
* Tasks that require additional arguments are easier to run with `g` script example: `g dnabrowser --random`


### Having issues, problems, or suggested improvements?
* Submit an [issue](https://github.com/nusco/narjillos/issues)
* Submit a [pull request](https://github.com/nusco/narjillos/pulls)
