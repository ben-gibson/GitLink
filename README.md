<h1 align="center">
  GitLink
  <br>
</h1>

<h4 align="center">
    A Jetbrains plugin that provides shortcuts to open a file or commit in Stash, GitHub, BitBucket or GitLab using the default browser or copy the link to the clipboard.
</h4>

<p align="center">
  <a href="https://travis-ci.org/ben-gibson/GitLink">
    <img src="https://travis-ci.org/ben-gibson/GitLink.svg?branch=master"
         alt="Gitter">
  </a>
  <a href="https://gitter.im/jetbrains-open-in-git-host/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge"><img src="https://badges.gitter.im/jetbrains-open-in-git-host/Lobby.svg"></a>
  <br>
  <img src="gitlink-demo.gif" alt="demo">
</p>
<br>

Installation
-------------------------------------------------------------------------------

This plugin is published on the
[JetBrains Plugin Repository](https://plugins.jetbrains.com/plugin/8183):

    Preferences → Plugins → Browse Repositories → Search for "GitLink"

### From Source

Clone this repository:

    $ git clone https://github.com/ben-gibson/GitLink
    $ cd GitLink

Update the permissions:

     $ chmod +x ./gradlew

Build the plugin zip file:

    $ ./gradlew buildPlugin

Install the plugin from `./build/distributions/GitLink-2.*.zip`:

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

After installing the plugin set your remote host (GitHub, GitLab, BitBucket, Stash) and enabled extensions in the preferences:

      Preferences → Other Settings → GitLink
      
Make sure you have registered your projects root under the version control preferences:

      Preferences → Version Control (see unregistered roots)

To open the current file in the default browser:

      View → Open in (your selected host) or
      Select in... → Browser (GitLink)

To copy the link to you clipboard:

    View → Copy (your selected host) link to clipboard

The current branch is used unless it does not exist on the remote in which case it defaults to your preferred branch as defined in the plugin settings.

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
