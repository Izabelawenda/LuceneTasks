<h2>Exercise:</h2>
Complete the bulk request below without using a text2 field in the last line:
```
POST my_second_index/_bulk

{"index": { ... }}

{"text1": "Test_text", "text2": "Another_test_text"}

{"update": {...}}

… // 1 line
```
so that after its execution the following query returns 1 document:
```
GET my_second_index/_search

{

  "query": {

    "bool": {

      "must": [

        {"term": {"text1.keyword": "Test_text_UPDATED"}},

        {"term": {"text2.keyword": "Another_test_text"}},

        {"term": {"text3.keyword": "Just_one_more_text"}}

      ]

    }}}
```
I.e.

- The text1 field should contain value “Test_text_UPDATED”
- The text2 field should contain value “Another_test_text”
- The text3 field should contain value “Just_one_more_text”

<h2>Solution:</h2>

```
POST my_second_index/_bulk
{"index": { "_index": "my_second_index", "_id": "1" }}
{"text1": "Test_text", "text2": "Another_test_text"}
{"update": { "_index": "my_second_index", "_id": "1" }}
{"doc": {"text1": "Test_text_UPDATED", "text3": "Just_one_more_text"}}
```

After request - `GET my_second_index/_search`:

```
{
  "took": 1,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 1,
      "relation": "eq"
    },
    "max_score": 1,
    "hits": [
      {
        "_index": "my_second_index",
        "_id": "1",
        "_score": 1,
        "_source": {
          "text1": "Test_text_UPDATED",
          "text2": "Another_test_text",
          "text3": "Just_one_more_text"
        }
      }
    ]
  }
}
```
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Another possible request:

`GET my_second_index/_doc/1`

```
GET my_second_index/_search
{
    "query": {
        "bool": {
            "must": [
                {"term": {"text1.keyword": "Test_text_UPDATED"}},
                {"term": {"text2.keyword": "Another_test_text"}},
                {"term": {"text3.keyword": "Just_one_more_text"}}
            ]
        }
      
    }
}
```