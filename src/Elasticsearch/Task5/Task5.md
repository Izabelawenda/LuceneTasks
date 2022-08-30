<h1>Exercises:</h1>

1. Create a search request for  the products index using a fuzzy query (not a match query with fuzziness) by the name.text field only, that returns all 6 documents.
2. Create a search request for  the products index using only one match query with query = WITH that returns all 6 documents. There should be no other parameters in the query.
3. Create a search request for  the products index using only one match query by the category_path field that contains more than 1 word in the query  and returns all 6 documents.
4. Create a search request  for the products index using only one match query by the category_path field that returns only 2 documents.
5. Create a search request for  the products index using a match_prase query with parameter query = SILVER METALLIC that returns 3 documents.

<h3>Solutions:</h3>

1. shoes and shield word search shies with edit distance of 2


    GET products/_search
    {
      "query": {
        "fuzzy" : {
          "name.text": {
            "value": "shies",
            "fuzziness": 2
    }}}}

2. description.text field to lowercase first our query


    GET products/_search
    {
      "query": {
        "match": {
          "description.text": "WITH"
    }}}

3. Field category_path uses path-analyzer. To check how it tokenizes text we can use request:


    GET products/_analyze
    {
    "analyzer": "path-analyzer",
    "text": "Women/Women's sneakers & shoes/Women's outdoor shoes"
    }

Tokens:

    {
    "tokens": [
    {
    "token": "Women",
    "start_offset": 0,
    "end_offset": 5,
    "type": "word",
    "position": 0
    },
    {
    "token": "Women/Women's sneakers & shoes",
    "start_offset": 0,
    "end_offset": 30,
    "type": "word",
    "position": 0
    },
    {
    "token": "Women/Women's sneakers & shoes/Women's outdoor shoes",
    "start_offset": 0,
    "end_offset": 52,
    "type": "word",
    "position": 0
    }
    ]
    }

Search request:

    GET products/_search
    {
      "query": {
        "match": {
          "category_path": {
            "query": "Women/Women's sneakers & shoes"
    }}}}
    
4. 


    GET products/_search
    {
      "query": {
        "match": {
          "category_path": {
            "query": "Women/Women's sneakers & shoes/Women's outdoor shoes"
    }}}}

5. We need to use nested query to access color field. Slop set to 2 because we need to switch positions of words.


    GET products/_search
    {
      "query": {
        "nested": {
          "path": "skus",
          "query": {
            "match_phrase": {
              "skus.color.text": {
                "query": "SILVER METALLIC",
                "slop": 2
    }}}}}}


