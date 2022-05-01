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
    }
}
