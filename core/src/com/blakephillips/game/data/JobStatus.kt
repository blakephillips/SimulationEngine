package com.blakephillips.game.data

enum class JobStatus {
    IDLE,
    START_PENDING,
    RUNNING,
    FINISHED,
    INCOMPLETE,
    CANCELLED
}

object JobStatusUtility {
    private val IN_PROGRESS_JOB_STATUSES = setOf(
        JobStatus.START_PENDING,
        JobStatus.RUNNING
    )

    fun isInProgress(status: JobStatus) = IN_PROGRESS_JOB_STATUSES.contains(status)
}
