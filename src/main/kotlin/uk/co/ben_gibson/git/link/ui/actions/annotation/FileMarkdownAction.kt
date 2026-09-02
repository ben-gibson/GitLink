package uk.co.ben_gibson.git.link.ui.actions.annotation

import git4idea.annotate.GitFileAnnotation
import uk.co.ben_gibson.git.link.Context

class FileMarkdownAction(annotation: GitFileAnnotation) : AnnotationAction(annotation, Type.COPY_MARKDOWN, Context::FileAtCommit)
