// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

suite("test_unique_model_schema_value_change","p0") {
     def tbName = "test_unique_model_schema_value_change"

     //Test the unique model by adding a value column
     sql """ DROP TABLE IF EXISTS ${tbName} """
     def initTable = " CREATE TABLE IF NOT EXISTS ${tbName}\n" +
             "          (\n" +
             "              `user_id` LARGEINT NOT NULL COMMENT \"用户id\",\n" +
             "              `username` VARCHAR(50) NOT NULL COMMENT \"用户昵称\",\n" +
             "              `city` VARCHAR(20) COMMENT \"用户所在城市\",\n" +
             "              `age` SMALLINT COMMENT \"用户年龄\",\n" +
             "              `sex` TINYINT COMMENT \"用户性别\",\n" +
             "              `phone` LARGEINT COMMENT \"用户电话\",\n" +
             "              `address` VARCHAR(500) COMMENT \"用户地址\",\n" +
             "              `register_time` DATETIME COMMENT \"用户注册时间\"\n" +
             "          )\n" +
             "          UNIQUE KEY(`user_id`, `username`)\n" +
             "          DISTRIBUTED BY HASH(`user_id`) BUCKETS 1\n" +
             "          PROPERTIES (\n" +
             "          \"replication_allocation\" = \"tag.location.default: 1\",\n" +
             "          \"enable_unique_key_merge_on_write\" = \"true\"\n" +
             "          );"

     def initTableData = "insert into ${tbName} values(123456789, 'Alice', 'Beijing', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00')," +
             "               (234567890, 'Bob', 'Shanghai', 30, 1, 13998765432, 'No. 456 Street, Shanghai', '2022-02-02 12:00:00')," +
             "               (345678901, 'Carol', 'Guangzhou', 28, 0, 13724681357, 'No. 789 Street, Guangzhou', '2022-03-03 14:00:00')," +
             "               (456789012, 'Dave', 'Shenzhen', 35, 1, 13680864279, 'No. 987 Street, Shenzhen', '2022-04-04 16:00:00')," +
             "               (567890123, 'Eve', 'Chengdu', 27, 0, 13572468091, 'No. 654 Street, Chengdu', '2022-05-05 18:00:00')," +
             "               (678901234, 'Frank', 'Hangzhou', 32, 1, 13467985213, 'No. 321 Street, Hangzhou', '2022-06-06 20:00:00')," +
             "               (789012345, 'Grace', 'Xian', 29, 0, 13333333333, 'No. 222 Street, Xian', '2022-07-07 22:00:00');"

     //Test the unique model by adding a value column with VARCHAR
     sql initTable
     sql initTableData
     def getTableStatusSql = " SHOW ALTER TABLE COLUMN WHERE IndexName='${tbName}' ORDER BY createtime DESC LIMIT 1  "
     def errorMessage=""
     sql """ alter  table ${tbName} add  column province VARCHAR(20)  DEFAULT "广东省" AFTER username """
     def insertSql = "insert into ${tbName} values(123456689, 'Alice', '四川省', 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00');"
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")


     //Test the unique model by adding a value column with BOOLEAN
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column special_area BOOLEAN  DEFAULT "0" AFTER username """
     insertSql = "insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")



     //Test the unique model by adding a value column with TINYINT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column special_area TINYINT  DEFAULT "0" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")



     //Test the unique model by adding a value column with SMALLINT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column area_num SMALLINT  DEFAULT "999" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', 567, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")


     //Test the unique model by adding a value column with INT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column house_price INT  DEFAULT "999" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', 2, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")


     //Test the unique model by adding a value column with BIGINT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column house_price1 BIGINT  DEFAULT "99999991" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', 88889494646, 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")



     //Test the unique model by adding a value column with LARGEINT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column car_price LARGEINT  DEFAULT "9999" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', 555888555, 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00');"
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")




     //TODO Test the unique model by adding a value column with FLOAT
     errorMessage="errCode = 2, detailMessage = Default value will loose precision: 166.68"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} add  column phone FLOAT  DEFAULT "166.68" AFTER username """
          insertSql = " insert into ${tbName} values(123456689, 'Alice', 189.98, 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00');"
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)





     //TODO Test the unique model by adding a value column with DOUBLE
     //java.sql.SQLException:
     errorMessage="errCode = 2, detailMessage = Default value will loose precision: 166.689"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} add  column watch DOUBLE  DEFAULT "166.689" AFTER username """
          insertSql = " insert into ${tbName} values(123456689, 'Alice', 189.479, 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)





     //Test the unique model by adding a value column with DECIMAL
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column watch DECIMAL(38,10)  DEFAULT "16899.6464689" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', 16499.6464689, 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00');"
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")



     //Test the unique model by adding a value column with DATE
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column watch DATE  DEFAULT "1997-01-01" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', \"2024-01-01\", 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")




     //Test the unique model by adding a value column with DATETIME
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column anniversary DATETIME  DEFAULT "1997-01-01 00:00:00" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', \"2024-01-04 09:00:00\", 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")





     //Test the unique model by adding a value column with CHAR
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column teacher CHAR  DEFAULT "0" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', \"1\", 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00');  "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")



     //Test the unique model by adding a value column with STRING
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column comment STRING  DEFAULT "我是小说家" AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', '我是侦探家', 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00');  "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")

     //TODO Test the unique model by adding a value column with HLL
     errorMessage="errCode = 2, detailMessage = Can not assign aggregation method on column in Unique data model table: comment"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} add  column comment HLL HLL_UNION   AFTER username """
          insertSql = " insert into ${tbName} values(123456689, 'Alice', '2', 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00');  "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)


     //TODO Test the unique model by adding a value column with bitmap
     errorMessage="errCode = 2, detailMessage = Can not assign aggregation method on column in Unique data model table: device_id"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} add  column device_id   bitmap BITMAP_UNION  AFTER username """
          insertSql = " insert into ${tbName} values(123456689, 'Alice', to_bitmap(243), 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00');  "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")

     },errorMessage)



     //Test the unique model by adding a value column with Map
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column m   Map<STRING, INT>   AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', {'a': 100, 'b': 200}, 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00');  "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")



     //Test the unique model by adding a value column with JSON
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} add  column   j    JSON   AFTER username """
     insertSql = " insert into ${tbName} values(123456689, 'Alice', '{\"k1\":\"v31\", \"k2\": 300}', 'Yaan',  25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")


     /**
      *  Test the unique model by modify a value type
      */

     sql """ DROP TABLE IF EXISTS ${tbName} """
     initTable = " CREATE TABLE IF NOT EXISTS ${tbName}\n" +
             "          (\n" +
             "              `user_id` LARGEINT NOT NULL COMMENT \"用户id\",\n" +
             "              `username` VARCHAR(50) NOT NULL COMMENT \"用户昵称\",\n" +
             "              `is_teacher` BOOLEAN COMMENT \"是否是老师\",\n" +
             "              `city` VARCHAR(20) COMMENT \"用户所在城市\",\n" +
             "              `age` SMALLINT COMMENT \"用户年龄\",\n" +
             "              `sex` TINYINT COMMENT \"用户性别\",\n" +
             "              `phone` LARGEINT COMMENT \"用户电话\",\n" +
             "              `address` VARCHAR(500) COMMENT \"用户地址\",\n" +
             "              `register_time` DATETIME COMMENT \"用户注册时间\"\n" +
             "          )\n" +
             "          UNIQUE KEY(`user_id`, `username`)\n" +
             "          DISTRIBUTED BY HASH(`user_id`) BUCKETS 1\n" +
             "          PROPERTIES (\n" +
             "          \"replication_allocation\" = \"tag.location.default: 1\",\n" +
             "          \"enable_unique_key_merge_on_write\" = \"true\"\n" +
             "          );"

     initTableData = "insert into ${tbName} values(123456789, 'Alice', 0, 'Beijing', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00')," +
             "               (234567890, 'Bob', 0, 'Shanghai', 30, 1, 13998765432, 'No. 456 Street, Shanghai', '2022-02-02 12:00:00')," +
             "               (345678901, 'Carol', 1, 'Guangzhou', 28, 0, 13724681357, 'No. 789 Street, Guangzhou', '2022-03-03 14:00:00')," +
             "               (456789012, 'Dave', 0, 'Shenzhen', 35, 1, 13680864279, 'No. 987 Street, Shenzhen', '2022-04-04 16:00:00')," +
             "               (567890123, 'Eve', 0, 'Chengdu', 27, 0, 13572468091, 'No. 654 Street, Chengdu', '2022-05-05 18:00:00')," +
             "               (678901234, 'Frank', 1, 'Hangzhou', 32, 1, 13467985213, 'No. 321 Street, Hangzhou', '2022-06-06 20:00:00')," +
             "               (789012345, 'Grace', 0, 'Xian', 29, 0, 13333333333, 'No. 222 Street, Xian', '2022-07-07 22:00:00');"

     //TODO Test the unique model by modify a value type from BOOLEAN to TINYINT
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to TINYINT"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher TINYINT  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)


     //TODO Test the unique model by modify a value type from BOOLEAN to SMALLINT
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to SMALLINT"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher SMALLINT  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)


     //TODO Test the unique model by modify a value type from BOOLEAN to INT
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to INT"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher INT  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)



     //TODO Test the unique model by modify a value type from BOOLEAN to BIGINT
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to BIGINT"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher BIGINT  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)



     //TODO Test the unique model by modify a value type from BOOLEAN to FLOAT
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to FLOAT"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher FLOAT  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', 1.0, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)


     //TODO Test the unique model by modify a value type from BOOLEAN to DOUBLE
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to DOUBLE"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher DOUBLE  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', 1.0, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)


     //TODO Test the unique model by modify a value type from BOOLEAN to DECIMAL
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to DECIMAL32"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher DECIMAL  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', 1.0, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)


     //TODO Test the unique model by modify a value type from BOOLEAN to CHAR
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to CHAR"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher CHAR  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', '1', 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)


     //TODO Test the unique model by modify a value type from BOOLEAN to STRING
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to STRING"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher STRING  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', '1', 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)


     //TODO Test the unique model by modify a value type from BOOLEAN to VARCHAR
     errorMessage="errCode = 2, detailMessage = Can not change BOOLEAN to VARCHAR"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_teacher VARCHAR(32)  DEFAULT "0"  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', '1', 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)




     /**
      *  Test the unique model by modify a value type from TINYINT to other type
      */
     sql """ DROP TABLE IF EXISTS ${tbName} """
     initTable = " CREATE TABLE IF NOT EXISTS ${tbName}\n" +
             "          (\n" +
             "              `user_id` LARGEINT NOT NULL COMMENT \"用户id\",\n" +
             "              `username` VARCHAR(50) NOT NULL COMMENT \"用户昵称\",\n" +
             "              `is_student` TINYINT COMMENT \"是否是学生\",\n" +
             "              `city` VARCHAR(20) COMMENT \"用户所在城市\",\n" +
             "              `age` SMALLINT COMMENT \"用户年龄\",\n" +
             "              `sex` TINYINT COMMENT \"用户性别\",\n" +
             "              `phone` LARGEINT COMMENT \"用户电话\",\n" +
             "              `address` VARCHAR(500) COMMENT \"用户地址\",\n" +
             "              `register_time` DATETIME COMMENT \"用户注册时间\"\n" +
             "          )\n" +
             "          UNIQUE KEY(`user_id`, `username`)\n" +
             "          DISTRIBUTED BY HASH(`user_id`) BUCKETS 1\n" +
             "          PROPERTIES (\n" +
             "          \"replication_allocation\" = \"tag.location.default: 1\",\n" +
             "          \"enable_unique_key_merge_on_write\" = \"true\"\n" +
             "          );"

     initTableData = "insert into ${tbName} values(123456789, 'Alice', 1, 'Beijing', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00')," +
             "               (234567890, 'Bob', 1, 'Shanghai', 30, 1, 13998765432, 'No. 456 Street, Shanghai', '2022-02-02 12:00:00')," +
             "               (345678901, 'Carol', 1, 'Guangzhou', 28, 0, 13724681357, 'No. 789 Street, Guangzhou', '2022-03-03 14:00:00')," +
             "               (456789012, 'Dave', 1, 'Shenzhen', 35, 1, 13680864279, 'No. 987 Street, Shenzhen', '2022-04-04 16:00:00')," +
             "               (567890123, 'Eve', 0, 'Chengdu', 27, 0, 13572468091, 'No. 654 Street, Chengdu', '2022-05-05 18:00:00')," +
             "               (678901234, 'Frank', 0, 'Hangzhou', 32, 1, 13467985213, 'No. 321 Street, Hangzhou', '2022-06-06 20:00:00')," +
             "               (789012345, 'Grace', 1, 'Xian', 29, 0, 13333333333, 'No. 222 Street, Xian', '2022-07-07 22:00:00');"

     //TODO Test the unique model by modify a value type from TINYINT  to BOOLEAN
     errorMessage="errCode = 2, detailMessage = Can not change TINYINT to BOOLEAN"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_student BOOLEAN  DEFAULT "true" """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', false, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)




     //Test the unique model by modify a value type from TINYINT  to SMALLINT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} MODIFY  column is_student SMALLINT  """
     insertSql = "insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")


     //Test the unique model by modify a value type from TINYINT  to INT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} MODIFY  column is_student INT  """
     insertSql = "insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")


     //Test the unique model by modify a value type from TINYINT  to BIGINT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} MODIFY  column is_student BIGINT  """
     insertSql = "insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")

     //Test the unique model by modify a value type from TINYINT  to LARGEINT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} MODIFY  column is_student LARGEINT  """
     insertSql = "insert into ${tbName} values(123456689, 'Alice', 1, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")


     //Test the unique model by modify a value type from TINYINT  to FLOAT
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} MODIFY  column is_student FLOAT  """
     insertSql = "insert into ${tbName} values(123456689, 'Alice', 1.2, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")

     //Test the unique model by modify a value type from TINYINT  to DOUBLE
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} MODIFY  column is_student DOUBLE  """
     insertSql = "insert into ${tbName} values(123456689, 'Alice', 1.23, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")


     //TODO Test the unique model by modify a value type from TINYINT  to DECIMAL32
     errorMessage="errCode = 2, detailMessage = Can not change TINYINT to DECIMAL32"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_student DECIMAL  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', 1.23, 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")

     },errorMessage)

     //TODO Test the unique model by modify a value type from TINYINT  to CHAR
     errorMessage="errCode = 2, detailMessage = Can not change TINYINT to CHAR"
     expectException({
          sql initTable
          sql initTableData
          sql """ alter  table ${tbName} MODIFY  column is_student CHAR(15)  """
          insertSql = "insert into ${tbName} values(123456689, 'Alice', 'asd', 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
          waitForSchemaChangeDone({
               sql getTableStatusSql
               time 60
          }, insertSql, true,"${tbName}")
     },errorMessage)




     //Test the unique model by modify a value type from TINYINT  to VARCHAR
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} MODIFY  column is_student VARCHAR(100)  """
     insertSql = "insert into ${tbName} values(123456689, 'Alice', 'asd', 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")


     //Test the unique model by modify a value type from TINYINT  to STRING
     sql initTable
     sql initTableData
     sql """ alter  table ${tbName} MODIFY  column is_student STRING """
     insertSql = "insert into ${tbName} values(123456689, 'Alice', 'asd', 'Yaan', 25, 0, 13812345678, 'No. 123 Street, Beijing', '2022-01-01 10:00:00'); "
     waitForSchemaChangeDone({
          sql getTableStatusSql
          time 60
     }, insertSql, true,"${tbName}")
}
