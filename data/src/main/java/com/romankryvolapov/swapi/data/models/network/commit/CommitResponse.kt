package com.romankryvolapov.swapi.data.models.network.commit

import com.google.gson.annotations.SerializedName

data class CommitResponse(
    val sha: String?,
    @SerializedName("node_id") val nodeId: String?,
    val commit: CommitData?,
    val url: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("comments_url") val commentsUrl: String?,
    val author: CommitUser?,
    val committer: CommitUser?,
    val parents: List<Parent>?
)

data class CommitData(
    val author: CommitAuthor?,
    val committer: CommitAuthor?,
    val message: String?,
    val tree: TreeInfo?,
    val url: String?,
    @SerializedName("comment_count") val commentCount: Int?,
    val verification: Verification?
)

data class CommitAuthor(
    val name: String?,
    val email: String?,
    val date: String?
)

data class TreeInfo(
    val sha: String?,
    val url: String?
)

data class Verification(
    val verified: Boolean?,
    val reason: String?,
    val signature: String?,
    val payload: String?,
    @SerializedName("verified_at") val verifiedAt: String?
)

data class CommitUser(
    val login: String?,
    val id: Long?,
    @SerializedName("node_id") val nodeId: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("gravatar_id") val gravatarId: String?,
    val url: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("followers_url") val followersUrl: String?,
    @SerializedName("following_url") val followingUrl: String?,
    @SerializedName("gists_url") val gistsUrl: String?,
    @SerializedName("starred_url") val starredUrl: String?,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String?,
    @SerializedName("organizations_url") val organizationsUrl: String?,
    @SerializedName("repos_url") val reposUrl: String?,
    @SerializedName("events_url") val eventsUrl: String?,
    @SerializedName("received_events_url") val receivedEventsUrl: String?,
    val type: String?,
    @SerializedName("site_admin") val siteAdmin: Boolean?
)

data class Parent(
    val sha: String?,
    val url: String?,
    @SerializedName("html_url") val htmlUrl: String?
)