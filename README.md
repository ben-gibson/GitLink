# Remote Repository Mapper

[![Build Status](https://travis-ci.org/ben-gibson/remote-repository-mapper.svg?branch=master)](https://travis-ci.org/ben-gibson/remote-repository-mapper)

A Jetbrains plugin that opens a local file under git version control in its remote origin repository.

Installation
-------------------------------------------------------------------------------

This plugin is published on the
[JetBrains Plugin Repository](https://plugins.jetbrains.com/plugin/8183):

    Preferences → Plugins → Browse Repositories → Search for "Remote Repository Mapper"

### From Source

Clone this repository:

    $ git clone https://github.com/ben-gibson/remote-repository-mapper
    $ cd remote-repository-mapper

Build the plugin zip file:

    $ chmod +x ./gradlew
    $ ./gradlew buildPlugin

Install the plugin from `./build/distributions/RemoteRepositoryMapper.zip`:

    Preferences → Plugins → Install plugin from disk


Development
-------------------------------------------------------------------------------

Execute an IntelliJ IDEA instance with the plugin you're developing installed

    $ chmod +x ./gradlew
    $ ./gradlew runIdea
    
Run the tests

    $ chmod +x ./gradlew
    $ ./gradlew test

Usage
-------------------------------------------------------------------------------

After installing, adjust the settings to match your remote repository provider

      Preferences → Other Settings → Remote Repository Mapper
      
Make sure you have registered your projects root under the version control settings.

      Preferences → Version Control (see unregistered roots)

Open a file that is under git version control in the editor

      View → Open in Git host

The current checked out branch is used unless it does not track a remote branch, in which case it defaults to using master.
The resulting link can be copied to the clipboard depending on your settings.

Change log
-------------------------------------------------------------------------------

Please see [CHANGELOG](CHANGELOG.md) for more information what has changed recently.

Contributing
-------------------------------------------------------------------------------

Please see [CONTRIBUTING](CONTRIBUTING.md) for details.

### Credits

License
-------------------------------------------------------------------------------

Please see [LICENSE](LICENSE) for details.