package uk.co.ben_gibson.git.link.ui.actions.annotation

import git4idea.annotate.GitFileAnnotation
import uk.co.ben_gibson.git.link.Context

class CommitBrowserAction(annotation: GitFileAnnotation) : AnnotationAction(annotation, Type.BROWSER, Context::Commit)
