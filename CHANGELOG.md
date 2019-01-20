# Changelog

All Notable changes to `GitLink` will be documented in this file

## 1.5.4 - 2016-07-29

- Fixed issue with branch encoding for `GitHub` and `GitLab` hosts.
- Fixed issue with origins using `git` protocol.

## 1.5.5 - 2016-09-19

- Run plugin process on a background thread to prevent UI freezes.

## 1.5.6 - 2016-09-26

- Ability to open a specific commit from the VCS log tool window.

## 1.5.7 - 2016-12-05

-  Fixed: Select target action now uses the correct line number when used from the editor. (#24 PR by markiewb)

## 1.6.0 - 2016-12-08

- Support GitBlit #26

## 1.6.1 - 2016-12-17

- Support more shortcuts.
- Added host icons.
- Minor refactors.

## 1.6.2 - 2016-12-17

- Added analytics

## 1.6.3- 2016-12-18

- Improved analytics

## 1.6.4- 2016-12-30

- Tweaked analytics

## 1.6.5 - 2017-03-24

- Removed analytics.

## 1.6.6 - 2017-04-12

- Fixed incompatibility issue with save actions plugin. Note that you have to configure the plugin again!

## 2.0.0 - 2017-09-17

- Rebuilt the entire plugin! Note that you have to configure the plugin again!

## 2.0.1 - 2017-09-30

- Fixed encoding issue when URL contains non-ASCII characters. #40

## 2.1.0 - 2017-11-05

- Code refactor
- Separated shortcuts for opening in the browser and copying to the clipboard #47
- Rename plugin to GitLink #46
- Make default branch customisable #45
- Add custom URL factory #44

## 2.1.1 - 2017-11-09

- Fixed force HTTPS option.

## 2.1.2 - 2017-11-15

- Fixed issue preventing port numbers with more than 4 digits being removed #52.


## 2.2.0 - 2017-11-10

- Added support for GitBlit. #41

## 2.3.0 - 2018-03-17

- Generate link to file at commit instead of branch where possible. #61
- Added actions to annotation gutter. #57
- Allow remote name to be configured from the preferences. #60
- Minor bug fixes and improvements.

## 2.3.1 - 2018-03-22

- Fixed bug which caused an incorrect URL to be created from the VCS log.
- Added GitBlit support to open a file at a specific commit. #65

## 2.4.0 - 2018-10-28

- Add open commit action to annotation gutter. #70
- Respect line number when using from the annotation gutter. #68
- Removed copy link action from annotation gutter.

## 3.0.0 - 2019-01-20

- Added ability to disable the plugin per project. #79
- Added support for hosts Giea and Gogs. #80
- Removed copy link action.
- Code base cleanup.
