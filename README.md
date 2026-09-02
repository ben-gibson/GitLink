# GitLink

![Build](https://github.com/ben-gibson/GitLink/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/8183-gitlink.svg)](https://plugins.jetbrains.com/plugin/8183-gitlink)
[![Rating](https://img.shields.io/jetbrains/plugin/r/stars/8183-gitlink.svg)](https://plugins.jetbrains.com/plugin/8183-gitlink)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/8183-gitlink.svg)](https://plugins.jetbrains.com/plugin/8183-gitlink)
[![Stand With Ukraine](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/badges/StandWithUkraine.svg)](https://stand-with-ukraine.pp.ua)

<!-- Plugin description -->

A [Jetbrains plugin](https://plugins.jetbrains.com/plugin/8183-gitlink) providing shortcuts to open or copy a file, directory or commit in `GitHub`, `Bitbucket`, 
`Codeberg`, `GitLab`, `Gitee`, `Gitea`, `Forgejo`, `Gogs`, `Azure`, `sourcehut`, and `Gerrit`. Custom platforms can also be configured using the URL template syntax.

<!-- Plugin description end -->

<p align="center">
  <a href="https://plugins.jetbrains.com/plugin/8183-gitlink">
    <video alt="Demo" src="./assets/demo.mp4" width="700" />
  </a>
</p>

## Usage

Install the plugin and configure your platform if it hasn't been auto-detected already:

      Preferences → Tools → GitLink

<p align="center">
  <img alt="Settings" src="./assets/settings-example.png" width="480" />
</p>

Make sure you have registered your projects root under the version control preferences:

      Preferences → Version Control (see unregistered roots)

The actions live under the Git menu and in the editor, project view and editor tab context menus:

      Git → Open in (your selected platform)
      Git → Copy (your selected platform) link
      Git → Copy (your selected platform) Markdown link

The current file can also be opened from the select-in popup:

      Select in... → (your selected platform)

Additional shortcuts are available from the editor gutter, the annotation gutter and the Git log window.

A URL can be generated in one of the following ways: 

* File at a commit
* File at a branch
* Commit

By default, when generating a URL to a file, the latest commit hash is used, creating a reference to a fixed version of 
the file's content. If the latest commit has not been pushed to the remote, the current branch is used instead. 
While this avoids generating a URL to a 404, it does mean the linked contents can change over time.

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "GitLink"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/ben-gibson/GitLink/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Support

* Star the repository
* [Rate the plugin](https://plugins.jetbrains.com/plugin/8183-gitlink)
* [Share the plugin](https://plugins.jetbrains.com/plugin/8183-gitlink)

  
## Change log

Please see [CHANGELOG](CHANGELOG.md) for more information what has changed recently.

## Contributing

Please see [CONTRIBUTING](CONTRIBUTING.md) for details.

## License

Please see [LICENSE](LICENSE) for details.
