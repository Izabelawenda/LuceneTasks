<h1>Exercise:</h1>

Create a request to the _analyze endpoint that tests analysis of text “I’m a hungry man” with a custom analyzer. This analyzer should use the standard tokenizer and the following token filters: lowercase, asciifolding, stop (without customization) and the shingle (with unigrams and shingle size from 2 to 4). There should be 9 tokens in the result.

<h3>Solution:</h3>

    GET _analyze
    {
        "tokenizer": "standard",
        "filter": [
            "lowercase",
            "asciifolding",
            "stop",
            {
                "type": "shingle",
                "min_shingle_size": 2,
                "max_shingle_size": 4,
                "output_unigrams": true
            }
            ],
        "text": "I'm a hungry woman"
    }

<h3>Output:</h3>

    {
        "tokens": [
        {
        "token": "i'm",
        "start_offset": 0,
        "end_offset": 3,
        "type": "<ALPHANUM>",
        "position": 0
        },
        {
        "token": "i'm _",
        "start_offset": 0,
        "end_offset": 6,
        "type": "shingle",
        "position": 0,
        "positionLength": 2
        },
        {
        "token": "i'm _ hungry",
        "start_offset": 0,
        "end_offset": 12,
        "type": "shingle",
        "position": 0,
        "positionLength": 3
        },
        {
        "token": "i'm _ hungry woman",
        "start_offset": 0,
        "end_offset": 18,
        "type": "shingle",
        "position": 0,
        "positionLength": 4
        },
        {
        "token": "_ hungry",
        "start_offset": 6,
        "end_offset": 12,
        "type": "shingle",
        "position": 1,
        "positionLength": 2
        },
        {
        "token": "_ hungry woman",
        "start_offset": 6,
        "end_offset": 18,
        "type": "shingle",
        "position": 1,
        "positionLength": 3
        },
        {
        "token": "hungry",
        "start_offset": 6,
        "end_offset": 12,
        "type": "<ALPHANUM>",
        "position": 2
        },
        {
        "token": "hungry woman",
        "start_offset": 6,
        "end_offset": 18,
        "type": "shingle",
        "position": 2,
        "positionLength": 2
        },
        {
        "token": "woman",
        "start_offset": 13,
        "end_offset": 18,
        "type": "<ALPHANUM>",
        "position": 3
        }
        ]
    }