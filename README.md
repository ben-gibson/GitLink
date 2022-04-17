# GitLink

![Build](https://github.com/ben-gibson/GitLink/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/8183-gitlink.svg)](https://plugins.jetbrains.com/plugin/8183-gitlink)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/8183-gitlink.svg)](https://plugins.jetbrains.com/plugin/8183-gitlink)

<!-- Plugin description -->

A Jetbrains plugin providing shortcuts to open or copy resources such as a file or commit in `GitHub`, `Bitbucket`, 
`GitLab`, `Gitea`, `Gogs` or `GitBlit`. Custom hosts can also be configured using the URL template
syntax.

<!-- Plugin description end -->

## Usage

Install the plugin and configure your remote host if it hasn't been auto detected already:

      Preferences → Tools → GitLink

Make sure you have registered your projects root under the version control preferences:

      Preferences → Version Control (see unregistered roots)

To open the current file in the default browser:

      View → Open in (your selected host) or
      Select in... → Browser (GitLink)

Additional shortcuts are als available from the annotation gutter, VCS log window and select in window.

A URL can be generated to one of the following resources. 

* Fle at commit
* File at branch
* Commit

By default, when generating a URL for a file, the latest commit will be included as part of the URL to provide a 
fixed snapshot of the file. If the latest commit is unknown or not on the remote, the branch will be used instead. 

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "GitLink"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/ben-gibson/GitLink/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

  
## Change log

Please see [CHANGELOG](CHANGELOG.md) for more information what has changed recently.

## Contributing

Please see [CONTRIBUTING](CONTRIBUTING.md) for details.

## License

Please see [LICENSE](LICENSE) for details.
