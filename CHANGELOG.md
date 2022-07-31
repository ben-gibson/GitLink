<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# GitLink Changelog

## [Unreleased]
- Replace Java URL and URI with URL library.
- Fix issue preventing registered custom domains being removed.

## [4.1.8]
- Rename 'Host' option to 'Platform'

## [4.1.7]
- Do not disable actions during project indexing

## [4.1.6]
- Trim forward slash character in file path substitution

## [4.1.5]
- Revert host detection fix

## [4.1.4]
- Strip user info component from the generated URL

## [4.1.3]
- Internal refactor

## [4.1.2]
- Fix host detection when opening a new project within the IDE.

## [4.1.1]
- Make 'main' the default fallback branch, replacing 'master'
- Fix issue validation custom host URL templates
- Don't allow duplicate domain registration

## [4.1.0]
- Allow custom domain registration for supported hosts.

## [4.0.9]
- Add Gitee host

## [4.0.8]
- Add host poll notification
- Add 'Do not show again' button to performance tip notification

## [4.0.7]
- Add experimental host Chromium
- Add resolve context middleware
- Dismiss notifications when action pressed

## [4.0.6]
- Fix Azure DevOps url templates

## [4.0.5]
- Fix line selection around collapsed code sections

## [4.0.4]
- Add pipeline to handle URL logic
- Fix typos

## [4.0.3]
- Add support for Ukraine in README
- Add notification on successful copy action
- Improve notification messages
- Make some notifications sticky
- Add request for support notification

## [4.0.2]
- Add images to README
- Add Open Collective link to README
- Fix issue where the '#' character in a file name is not correctly encoded.

## [4.0.1]
- Fixed settings button opening the wrong menu.
- Add shortcuts to editor gutter. #166

## 4.0.0
- Completely re-written in Kotlin
- Improve settings UI
- Improve notifications
- Support the creation of multiple custom hosts
- Store custom hosts across projects
- Auto-detect host when opening a new project
- Many more fixes and improvements.

## 3.3.1
- Add preference to disable check for a commit's existence on the remote. #97

## 3.3.0
- Re-added support for copying links to the clipboard. #85

## 3.2.0
- Rebuilt the substitution system used for the Custom host type. This system is now also used under the hood for most
  pre-defined host types. #92

## 3.1.2
- Fix issue resulting in an invalid URL for project/organisation names made up digits when the remote URL uses
  the SSH protocol in the SCP syntax. #94

## 3.1.1
- Fix multi-line selection in GitLab. #86

## 3.1.0
- Support for multiline selection. #77
- Renamed the host Stash to Bitbucket Server

## 3.0.0
- Added ability to disable the plugin per project. #79
- Added support for hosts Giea and Gogs. #80
- Removed copy link action.
- Code base cleanup.

## 2.4.0
- Add open commit action to annotation gutter. #70
- Respect line number when using from the annotation gutter. #68
- Removed copy link action from annotation gutter.

## 2.3.1
- Fixed bug which caused an incorrect URL to be created from the VCS log.
- Added GitBlit support to open a file at a specific commit. #65

## 2.3.0
- Generate link to file at commit instead of branch where possible. #61
- Added actions to annotation gutter. #57
- Allow remote name to be configured from the preferences. #60
- Minor bug fixes and improvements.

## 2.2.0
- Added support for GitBlit. #41

## 2.1.2
- Fixed issue preventing port numbers with more than 4 digits being removed #52.

## 2.1.1
- Fixed force HTTPS option.

## 2.1.0
- Code refactor
- Separated shortcuts for opening in the browser and copying to the clipboard #47
- Rename plugin to GitLink #46
- Make default branch customisable #45
- Add custom URL factory #44

## 2.0.1
- Fixed encoding issue when URL contains non-ASCII characters. #40

## 2.0.0
- Rebuilt the entire plugin! Note that you have to configure the plugin again!

## 1.6.6
- Fixed incompatibility issue with save actions plugin. Note that you have to configure the plugin again!

## 1.6.5
- Removed analytics.

## 1.6.4
- Tweaked analytics

## 1.6.3
- Improved analytics

## 1.6.2
- Added analytics

## 1.6.1
- Support more shortcuts.
- Added host icons.
- Minor refactors.

## 1.6.0
- Support GitBlit #26

## 1.5.7
-  Fixed: Select target action now uses the correct line number when used from the editor. (#24 PR by markiewb)

## 1.5.6
- Ability to open a specific commit from the VCS log tool window.

## 1.5.5
- Run plugin process on a background thread to prevent UI freezes.

## 1.5.4
- Fixed issue with branch encoding for `GitHub` and `GitLab` hosts.
- Fixed issue with origins using `git` protocol.