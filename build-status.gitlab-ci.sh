#!/bin/sh

REPO_URL=$(jq -r '.pullrequest.source.repository.full_name' "$TRIGGER_PAYLOAD")

# PR information
PR_ID=$(jq -r '.pullrequest.id' "$TRIGGER_PAYLOAD")

# Branch details
TARGET_BRANCH_NAME=$(jq -r '.pullrequest.destination.branch.name' "$TRIGGER_PAYLOAD")
SOURCE_BRANCH_NAME=$(jq -r '.pullrequest.source.branch.name' "$TRIGGER_PAYLOAD")

MERGE_STATUS=$(jq -r '.pullrequest.state' "$TRIGGER_PAYLOAD")

if [ "$MERGE_STATUS" != "MERGED" ]; then

    if [ "$SOURCE_BRANCH_NAME" == "null" ]; then

        SOURCE_BRANCH_NAME=$(jq -r '.push.changes[0].old.name' "$TRIGGER_PAYLOAD")
    fi
    if [ "$SOURCE_BRANCH_NAME" == "null" ]; then
        SOURCE_BRANCH_NAME=$CI_COMMIT_REF_NAME
    fi
else
    SOURCE_BRANCH_NAME=$TARGET_BRANCH_NAME
fi

SOURCE_COMMIT_SHA=$(jq -r '.pullrequest.source.commit.hash' "$TRIGGER_PAYLOAD")

send_status_request() {
    STATUS=$1
    SRC_BRANCH=$2
    SRC_TYPE=$3

    echo "{\"state\":\"$STATUS\",\"key\":\"gitlab-pipeline\",\"name\":\"$SRC_TYPE Build & deploy\",\"url\":\"$CI_PIPELINE_URL\",\"description\":\"PR $PR_ID: $SRC_BRANCH → $TARGET_BRANCH_NAME\"}" >build.json

    curl --request POST \
        --header 'Accept: application/json' \
        --header 'Content-Type: application/json' \
        --header "Authorization: Bearer $BEARER_TOKEN" \
        --url https://api.bitbucket.org/2.0/repositories/"$REPO_URL"/commit/"$SOURCE_COMMIT_SHA"/statuses/build/ \
        -d @build.json

    rm build.json
}

status() {
    STATUS=$1

    if [ "$MERGE_STATUS" == "OPEN" ]; then
        send_status_request "$STATUS" "$SOURCE_BRANCH_NAME" "PR"
    fi

    if [ "$SOURCE_BRANCH_NAME" ] && [ "$TARGET_BRANCH_NAME" == "master" ]; then
        TO_MASTER_SOURCE_BRANCH_NAME=$(jq -r '.pullrequest.source.branch.name' "$TRIGGER_PAYLOAD")
        SOURCE_COMMIT_SHA=$MASTER_HASH
        if [ "$MERGE_STATUS" == "MERGED" ]; then
            send_status_request "$STATUS" "$TO_MASTER_SOURCE_BRANCH_NAME" "MASTER"
        fi
    fi
}
