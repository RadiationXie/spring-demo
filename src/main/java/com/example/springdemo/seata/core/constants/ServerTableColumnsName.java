package com.example.springdemo.seata.core.constants;

public interface ServerTableColumnsName {
  /**
   * The constant global_table column name xid
   */
  String GLOBAL_TABLE_XID = "xid";

  /**
   * The constant global_table column name transaction_id
   */
  String GLOBAL_TABLE_TRANSACTION_ID = "transaction_id";

  /**
   * The constant global_table column name status
   */
  String GLOBAL_TABLE_STATUS = "status";

  /**
   * The constant global_table column name application_id
   */
  String GLOBAL_TABLE_APPLICATION_ID = "application_id";

  /**
   * The constant global_table column name transaction_service_group
   */
  String GLOBAL_TABLE_TRANSACTION_SERVICE_GROUP = "transaction_service_group";

  /**
   * The constant global_table column name transaction_name
   */
  String GLOBAL_TABLE_TRANSACTION_NAME = "transaction_name";

  /**
   * The constant global_table column name timeout
   */
  String GLOBAL_TABLE_TIMEOUT = "timeout";

  /**
   * The constant global_table column name begin_time
   */
  String GLOBAL_TABLE_BEGIN_TIME = "begin_time";

  /**
   * The constant global_table column name application_data
   */
  String GLOBAL_TABLE_APPLICATION_DATA = "application_data";

  /**
   * The constant global_table column name gmt_create
   */
  String GLOBAL_TABLE_GMT_CREATE = "gmt_create";

  /**
   * The constant global_table column name gmt_modified
   */
  String GLOBAL_TABLE_GMT_MODIFIED = "gmt_modified";






  /**
   * The constant branch_table column name branch_id
   */
  String BRANCH_TABLE_BRANCH_ID = "branch_id";

  /**
   * The constant branch_table column name xid
   */
  String BRANCH_TABLE_XID = "xid";

  /**
   * The constant branch_table column name transaction_id
   */
  String BRANCH_TABLE_TRANSACTION_ID = "transaction_id";

  /**
   * The constant branch_table column name resource_group_id
   */
  String BRANCH_TABLE_RESOURCE_GROUP_ID = "resource_group_id";

  /**
   * The constant branch_table column name resource_id
   */
  String BRANCH_TABLE_RESOURCE_ID = "resource_id";

  /**
   * The constant branch_table column name branch_type
   */
  String BRANCH_TABLE_BRANCH_TYPE = "branch_type";

  /**
   * The constant branch_table column name status
   */
  String BRANCH_TABLE_STATUS = "status";

  /**
   * The constant branch_table column name begin_time
   */
  String BRANCH_TABLE_BEGIN_TIME = "begin_time";

  /**
   * The constant branch_table column name application_data
   */
  String BRANCH_TABLE_APPLICATION_DATA = "application_data";

  /**
   * The constant branch_table column name client_id
   */
  String BRANCH_TABLE_CLIENT_ID = "client_id";

  /**
   * The constant branch_table column name gmt_create
   */
  String BRANCH_TABLE_GMT_CREATE = "gmt_create";

  /**
   * The constant branch_table column name gmt_modified
   */
  String BRANCH_TABLE_GMT_MODIFIED = "gmt_modified";






  /**
   * The constant lock_table column name row_key
   */
  String LOCK_TABLE_ROW_KEY = "row_key";

  /**
   * The constant lock_table column name xid
   */
  String LOCK_TABLE_XID = "xid";

  /**
   * The constant lock_table column name transaction_id
   */
  String LOCK_TABLE_TRANSACTION_ID = "transaction_id";

  /**
   * The constant lock_table column name branch_id
   */
  String LOCK_TABLE_BRANCH_ID = "branch_id";


  /**
   * The constant lock_table column name resource_id
   */
  String LOCK_TABLE_RESOURCE_ID = "resource_id";

  /**
   * The constant lock_table column name table_name
   */
  String LOCK_TABLE_TABLE_NAME = "table_name";

  /**
   * The constant lock_table column name pk
   */
  String LOCK_TABLE_PK = "pk";

  /**
   * The constant lock_table column name status
   */
  String LOCK_TABLE_STATUS = "status";

  /**
   * The constant lock_table column name gmt_create
   */
  String LOCK_TABLE_GMT_CREATE = "gmt_create";

  /**
   * The constant lock_table column name gmt_modified
   */
  String LOCK_TABLE_GMT_MODIFIED = "gmt_modified";



  /**
   * The constant distributed_lock column name lock key
   */
  String DISTRIBUTED_LOCK_KEY = "lock_key";
  /**
   * The constant distributed_lock column name lock value
   */
  String DISTRIBUTED_LOCK_VALUE = "lock_value";
  /**
   * The constant distributed_lock column name expire
   */
  String DISTRIBUTED_LOCK_EXPIRE = "expire";
}
