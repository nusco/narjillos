# this buildfile is still broken. I'm fixing it a bit at a time, but for now
# your best option is to generate project files for your favourite IDE (either
# with "buildr eclipse" or "buildr idea") and use the IDE. To run stuff,
# you'll find dedicated scripts in the project root ("narjillos", "experiment",
# etc.

repositories.remote << 'http://repo1.maven.org/maven2'

LIBS = ['commons-cli:commons-cli:jar:1.2',
        'com.google.code.gson:gson:jar:2.3',
        'org.yaml:snakeyaml:jar:1.15']

desc 'The main project'
define 'narjillos' do
  project.version = File.read('version').strip
  project.group = 'org.nusco.narjillos'
  project.manifest['Copyright'] = 'See LICENSE file'

  compile.with LIBS

  # TODO: pass command-line arguments (incl. empty)
  run.using :main => 'org.nusco.narjillos.NarjillosRunner', :java_args => ['-Xmx8192M', '-Xms8192M']

  package :jar

  #ROOT_FILES = 'version', 'LICENSE', 'README.md', 'config.yaml'
  #package(:zip).include(ROOT_FILES, _('bin/'), _('target/narjillos-*.jar'), artifacts(LIBS))
end

# TODO: pass command-line arguments (incl. empty)
desc 'Runs an experiment (without graphics)'
task 'experiment' => :compile do
  javaclasses.org.nusco.narjillos.ExperimentRunner.main([])  #, :java_args => ['-Xmx8192M', '-Xms8192M']
end

desc 'Runs ancestry analysis on an .exp file'
task 'ancestry', [:file] => :compile do |t, args|
  javaclasses.org.nusco.narjillos.Ancestry.main([args[:file]]) #, :java_args => ['-Xmx8192M', '-Xms8192M']
end

desc "Prints the current backlog (usage: backlog[min_lines])"
task 'backlog', [:min_lines] => :compile do |t, args|
  javaclasses.org.nusco.narjillos.Backlog.main([args[:min_lines] || "10"])
end

desc "Prints the first few lines of the current backlog"
task 'bl' => :backlog

def javaclasses()
  Java.classpath << LIBS + ["target/classes"]
  Java.load
  Java
end

desc 'Runs the performance test'
task 'test:performance' => :compile do
  javaclasses.org.nusco.narjillos.PerformanceTest
end

desc 'Runs the (slow) test for deterministic experiment'
task 'test:deterministic' => :compile do
  javaclasses.org.nusco.narjillos.DeterministicExperimentTest
end

# TODO: override the normal test and call it test:unit. Do something similar for test:failed
desc 'Runs all the tests'
task 'test:all' => ['test', 'test:deterministic', 'test:performance']

# TODO: package JAR with scripts (in BIN) and other files (in root) in a ZIP
#       files: 'config.yaml', 'LICENSE', 'README.md', 'version'

# TODO: add generated scripts from previous builds with '-Xmx8192M', maybe '-Xms8192M' for 'narjllos'
# (PetriApp) and 'experiment' (PetriDish)

# TODO: do I really need both Xmx and Xms in the run tasks?
