{
    "id": 101,
    "User": "system",
    "Priority": 3,
    "Status": "pending",
    "Return": false,
    "Loop": false,
    "Queue": [
        {
            "Id": 1,
            "Name": "UIFace",
            "Status": "pending",
            "Data": "smile",
            "Mode": "unblock",
            "Result": null,
            "LoopGrade": null
        },
        {
            "Id": 1,
            "Name": "VoicePlay",
            "Status": "pending",
            "Data": "hello",
            "Mode": "block",
            "Result": null,
            "LoopGrade": null
        },
        {
            "Id": 2,
            "Name": "SemanticUnderstanding",
            "Status": "pending",
            "Data": "StartListening",
            "Mode": "block",
            "Result": null,
            "LoopGrade": null
        },
        {
            "Id": 3,
            "Name": "master",
            "Status": "pending",
            "Data": "RESULT_SEMANTIC_UNDERSTANDING",
            "Mode": "unblock",
            "Result": "SemanticUnderstanding",
            "LoopGrade": null
        }
    ]
}