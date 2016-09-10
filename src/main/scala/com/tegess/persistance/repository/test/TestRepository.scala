package com.tegess.persistance.repository.test

import com.avsystem.commons.mongo.{BsonCodec, Doc, DocKey}
import com.mongodb.client.MongoCollection
import com.tegess.domain.test.Test
import org.bson.{BsonArray, BsonDocument, BsonString}

class TestRepository(val collection: MongoCollection[Test]) {


  def insert(test: Test): Unit = {
    collection.insertOne(test)
  }


}

object TestRepository {
  import com.avsystem.commons.mongo.BsonCodec._

  val nameKey = DocKey("name", BsonCodec.string)
  val ageKey = DocKey("age", BsonCodec.int32)
  val listKey: DocKey[List[Int], BsonArray] = DocKey[List[Int], BsonArray]("list", BsonCodec.int32.collection)

  val testCodec = create[Test, BsonDocument](
    document => {
      val doc = new Doc(document)
      new Test(doc.require(nameKey), doc.require(ageKey), doc.require(listKey))
    },
    test => Doc().put(nameKey, test.name).put(ageKey, test.age).put(listKey, test.list).toBson
  )

}
