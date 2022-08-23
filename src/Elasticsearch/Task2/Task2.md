<h1>Exercises:</h1>

1. Create a search request to the products index using a match_all query that returns only 3 documents.

<h3>Solution:</h3>

    GET products/_search
    {
        "size": 3,
        "query": {
            "match_all": {}
        }
    }

2. Create a search request to the products index using a range query by the price field that returns only 2 documents with _id = 3 and 5.

<h3>Solution:</h3>

Products with id 3 and 5 were the only ones within the range of 105 and 110.

    GET products/_search
    {
        "query": {
            "range": {
                "price": {
                    "gte": 105,
                    "lte": 110
                }
            }
        }
    }

3. In the following query:

    
    GET products/_search
    {
    "query": {
        "bool": {
          "filter": {"exists": {"field": "id"}},
          "should": [
            {"term": {"id": 1}},
            {"term": {"id": 2}},
            {"term": {"id": 3}}
          ],
          "minimum_should_match": ...
        }}}
    

set minimum_should_match:

1. To positive value, so that this request returns 0 documents
2. To negative value, so that this request returns 3 documents
3. To negative value, so that this request returns 0 documents
4. To negative value, so that this request returns 6 documents
5. To percentage negative value > -95% and < -5%, so that this request returns 3 documents.

<h3>Solution:</h3>

1. `>= 2`
2. `-2`
3. `-1`
4. `-3`
5. `>= -95% and <= -67%`

