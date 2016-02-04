# Remote Repository Mapper

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

    $ ./gradlew buildPlugin

Install the plugin from `./build/distributions/RemoteRepositoryMapper.zip`:

    Preferences → Plugins → Install plugin from disk


Development
-------------------------------------------------------------------------------

Execute an IntelliJ IDEA instance with the plugin you're developing installed

    $ ./gradlew runIdea

Usage
-------------------------------------------------------------------------------

After installing, adjust the settings to match your remote repository provider

      Preferences → Other Settings → Remote Repository Mapper

Open a file that is under git version control in the editor

      File → Open in remote repository

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