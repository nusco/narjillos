# this buildfile is still broken. I'm fixing it a bit at a time, but for now
# your best option is to generate project files for your favourite IDE (either
# with "buildr eclipse" or "buildr idea") and use the IDE.

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
  run.using :main => 'org.nusco.narjillos.NarjillosRunner', :java_args => ['-Xmx4g']

  package :jar

  # FIXME: packaging is broken
  ROOT_FILES = 'version', 'LICENSE', 'README.md', 'config.yaml', 'narjillos', 'narjillos.bat', 'experiment', 'experiment.bat'
  package(:zip).include(ROOT_FILES, _('target/narjillos-*.jar'), artifacts(LIBS))
end

# TODO: pass command-line arguments (incl. empty)
desc 'Runs an experiment (without graphics)'
task 'experiment' => :compile do
  # FIXME: why does this line break the package task?
  javaclasses.org.nusco.narjillos.ExperimentRunner.main([])
end

desc 'Runs ancestry analysis on an .exp file'
task 'ancestry', [:file] => :compile do |t, args|
  javaclasses.org.nusco.narjillos.Ancestry.main([args[:file]])
end

# Backlog

desc "Prints the top of the backlog"
task 'backlog' => :compile do
  javaclasses.org.nusco.narjillos.Backlog.main(["10"])
end

desc "Prints the whole backlog"
task 'backlog:all' => :compile do
  javaclasses.org.nusco.narjillos.Backlog.main([])
end

# Testing

desc 'Runs the performance test'
task 'test:performance' => :compile do
  javaclasses.org.nusco.narjillos.PerformanceTest
end

desc 'Runs the (slow) test for deterministic experiment'
task 'test:deterministic' => :compile do
  javaclasses.org.nusco.narjillos.DeterministicExperimentTest
end

desc 'Runs all the tests'
task 'test:all' => ['test', 'test:deterministic', 'test:performance']

# Helpers

def javaclasses()
  ENV['JAVA_OPTS'] ||= '-Xmx4g' # Large heap. Some tasks (like "experiment") need it.
  Java.classpath << LIBS + ["target/classes"]
  Java.load
  Java
end

