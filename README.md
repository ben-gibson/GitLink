# Open in Git host

[![Build Status](https://travis-ci.org/ben-gibson/jetbrains-open-in-git-host.svg?branch=master)](https://travis-ci.org/ben-gibson/jetbrains-open-in-git-host)
[![Join the chat at https://gitter.im/jetbrains-open-in-git-host/Lobby](https://badges.gitter.im/jetbrains-open-in-git-host/Lobby.svg)](https://gitter.im/jetbrains-open-in-git-host/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

A Jetbrains plugin that opens a local file under Git version control in its remote host using the default browser.
It can also optionally copy the URL to the clipboard.

Installation
-------------------------------------------------------------------------------

This plugin is published on the
[JetBrains Plugin Repository](https://plugins.jetbrains.com/plugin/8183):

    Preferences → Plugins → Browse Repositories → Search for "Open in Git host"

### From Source

Clone this repository:

    $ git clone https://github.com/ben-gibson/jetbrains-open-in-git-host
    $ cd jetbrains-open-in-git-host

Update the permissions:

     $ chmod +x ./gradlew

Build the plugin zip file:

    $ ./gradlew buildPlugin

Install the plugin from `./build/distributions/RemoteRepositoryMapper.zip`:

    Preferences → Plugins → Install plugin from disk


Development
-------------------------------------------------------------------------------

Update the permissions:

     $ chmod +x ./gradlew

Execute an IntelliJ IDEA instance with the plugin you're developing installed:

    $ ./gradlew runIdea
    
Run the tests:

    $ ./gradlew test

Usage
-------------------------------------------------------------------------------

After installing the plugin set your remote host in the preferences:

      Preferences → Other Settings → Open in Git host
      
Make sure you have registered your projects root under the version control preferences:

      Preferences → Version Control (see unregistered roots)

Open a project file that is under Git version control in the editor:

      View → Open in Git host or
      Select in... → Open in Git host

The current branch is used unless it does not exist in the remote host in which case it defaults to using the master branch.
The resulting link can be copied to the clipboard depending on your plugin preferences.

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
