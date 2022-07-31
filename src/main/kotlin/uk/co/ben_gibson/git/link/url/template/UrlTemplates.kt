package uk.co.ben_gibson.git.link.url.template

data class UrlTemplates(val fileAtBranch: String, val fileAtCommit : String, val commit : String) {
    companion object {
        fun gitHub(): UrlTemplates {
            return UrlTemplates(
                "{remote:url}/{object}/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
                "{remote:url}/{object}/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
                "{remote:url}/commit/{commit}"
            )
        }

        fun gitLab(): UrlTemplates {
            return UrlTemplates(
                "{remote:url}/{object}/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-{line:end}{line-block:end}",
                "{remote:url}/{object}/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-{line:end}{line-block:end}",
                "{remote:url}/commit/{commit}"
            )
        }

        fun bitbucketCloud(): UrlTemplates {
            return UrlTemplates(
                "{remote:url}/src/{branch}/{file:path}/{file:name}{line-block:start}#lines-{line:start}:{line:end}{line-block:end}",
                "{remote:url}/src/{commit}/{file:path}/{file:name}{line-block:start}#lines-{line:start}:{line:end}{line-block:end}",
                "{remote:url}/commits/{commit}"
            )
        }

        fun bitbucketServer(): UrlTemplates {
            return UrlTemplates(
                "{remote:url:protocol}://{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at=refs/heads/{branch}{line-block:start}#{line:start}-{line:end}{line-block:end}",
                "{remote:url:protocol}://{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/browse/{file:path}/{file:name}?at={commit}{line-block:start}#{line:start}-{line:end}{line-block:end}",
                "{remote:url:protocol}://{remote:url:host}/projects/{remote:url:path:0}/repos/{remote:url:path:1}/commits/{commit}"
            )
        }

        fun gitea(): UrlTemplates {
            return UrlTemplates(
                "{remote:url}/src/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
                "{remote:url}/src/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
                "{remote:url}/commit/{commit}"
            )
        }

        fun gogs(): UrlTemplates {
            return UrlTemplates(
                "{remote:url}/src/{branch}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
                "{remote:url}/src/{commit}/{file:path}/{file:name}{line-block:start}#L{line:start}-L{line:end}{line-block:end}",
                "{remote:url}/commit/{commit}"
            )
        }

        fun gitee() = UrlTemplates.gitHub()

        fun azure(): UrlTemplates {
            return UrlTemplates(
                "{remote:url}?version=GB{branch}&path=/{file:path}/{file:name}{line-block:start}&line={line:start}&lineEnd={line:end}{line-block:end}&lineStartColumn=1&lineEndColumn=1",
                "{remote:url}?version=GC{commit}&path=/{file:path}/{file:name}{line-block:start}&line={line:start}&lineEnd={line:end}{line-block:end}&lineStartColumn=1&lineEndColumn=1",
                "{remote:url}/commit/{commit}"
            )
        }
    }
}
