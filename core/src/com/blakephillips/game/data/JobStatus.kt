package com.blakephillips.game.data

enum class JobStatus {
    IDLE,
    START_PENDING,
    RUNNING,
    FINISHED,
    INCOMPLETE,
    CANCELLED
}

private val IN_PROGRESS_JOB_STATUSES = setOf(
    JobStatus.START_PENDING,
    JobStatus.RUNNING
)

fun JobStatus.isInProgress(): Boolean = IN_PROGRESS_JOB_STATUSES.contains(this)
