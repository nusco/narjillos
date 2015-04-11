repositories.remote << 'http://repo1.maven.org/maven2'

COMMONS_CLI = 'commons-cli:commons-cli:jar:1.2'
GSON = 'com.google.code.gson:gson:jar:2.3'
SNAKEYAML = 'org.yaml:snakeyaml:jar:1.15'
LIBS = [COMMONS_CLI, GSON, SNAKEYAML]

Project.local_task :experiment
Project.local_task :petri
Project.local_task :ancestry

desc 'The main project'
define 'narjillos' do
  project.version = File.read('version').strip
  project.group = 'org.nusco.narjillos'
  project.manifest['Copyright'] = 'Acme Inc (C) 2007'
  compile.options.target = '1.8'

  compile.with LIBS

  # TODO: pass command-line arguments (incl. empty)
  run.using :main => 'org.nusco.narjillos.NarjillosRunner', :java_args => ['-Xmx8192M', '-Xms8192M']

  package :jar

  #ROOT_FILES = 'version', 'LICENSE', 'README.md', 'config.yaml'
  #package(:zip).include(ROOT_FILES, _('bin/'), _('target/narjillos-*.jar'), artifacts(LIBS))

  # TODO: pass command-line arguments (incl. empty)
  desc 'Runs an experiment (without graphics)'
  define :experiment do
    run.using :main => 'org.nusco.narjillos.PetriDish', :java_args => ['-Xmx8192M', '-Xms8192M']
  end

  desc 'Runs ancestry analysis on an .exp file'
  define :ancestry do
    run.using :main => 'org.nusco.narjillos.Ancestry', :java_args => ['-Xmx8192M', '-Xms8192M']
  end
end

def javaclasses()
  Java.classpath << LIBS
  Java.classpath << "target/classes"
  Java.load
  Java
end

# TODO: command-line arguments or 10 by default
desc "Prints the current backlog"
task 'backlog' => :compile do
  javaclasses.org.nusco.narjillos.Backlog.main(["10"])
end

task 'bl' => :backlog


desc 'Runs the performance test'
task 'test:performance' => :compile do
  project(:narjillos).run.using :main => 'org.nusco.narjillos.PerformanceTest'
end

desc 'Runs the (slow) test for deterministic experiment'
task 'test:deterministic' => :compile do
  project(:narjillos).run.using :main => 'org.nusco.narjillos.DeterministicExperimentTest'
end

# TODO: override the normal 'test' and call it 'test:unit'. Do something similar for test:failed
desc 'Runs all the tests'
task 'test:all' => ['test', 'test:deterministic', 'test:performance']

# TODO: package JAR with scripts (in BIN) and other files (in root) in a ZIP
#       files: 'config.yaml', 'LICENSE', 'README.md', 'version'

# TODO: add generated scripts from previous builds with '-Xmx8192M', maybe '-Xms8192M' for 'narjllos'
# (PetriApp) and 'experiment' (PetriDish)

# TODO: do I really need both Xmx and Xms in the previous tasks?
