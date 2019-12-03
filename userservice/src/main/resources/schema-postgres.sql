-- ----------------------------
-- Sequence structure for seq_worker_node_id
-- ----------------------------
DROP SEQUENCE IF EXISTS "seq_worker_node_id";
CREATE SEQUENCE "seq_worker_node_id"
INCREMENT 1
MAXVALUE 9223372036854775807
CACHE 1;

-- ----------------------------
-- Table structure for t_app
-- ----------------------------
DROP TABLE IF EXISTS "t_app";
CREATE TABLE "t_app" (
  "app_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(255) COLLATE "pg_catalog"."default",
  "app_secret" varchar(255) COLLATE "pg_catalog"."default",
  "registered_redirect_uri" varchar(1024) COLLATE "pg_catalog"."default",
  "register_date" timestamptz(4),
  "enable" int4
)
;

-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS "t_group";
CREATE TABLE "t_group" (
  "id" int8 NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "create_date" timestamptz(4),
  "create_user_id" varchar(255) COLLATE "pg_catalog"."default",
  "last_modified_date" timestamptz(4),
  "last_modified_user_id" varchar(255) COLLATE "pg_catalog"."default",
  "app_id" varchar(255) COLLATE "pg_catalog"."default",
  "enable" int4
)
;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS "t_role";
CREATE TABLE "t_role" (
  "id" int8 NOT NULL,
  "role_name" varchar(255) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "create_date" timestamptz(4),
  "create_user_id" varchar(255) COLLATE "pg_catalog"."default",
  "last_modified_date" timestamptz(4),
  "last_modified_user_id" varchar(255) COLLATE "pg_catalog"."default",
  "enable" int4,
  "app_id" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS "t_user0";
CREATE TABLE "t_user0" (
  "create_date" timestamptz(4),
  "email" varchar(255) COLLATE "pg_catalog"."default",
  "id" int8 NOT NULL,
  "mobile" varchar(255) COLLATE "pg_catalog"."default",
  "nickname" varchar(255) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "username" varchar(255) COLLATE "pg_catalog"."default",
  "last_modified_date" timestamptz(4),
  "enable" int4,
  "head_image_id" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS "t_user1";
CREATE TABLE "t_user1" (
  "create_date" timestamptz(4),
  "email" varchar(255) COLLATE "pg_catalog"."default",
  "id" int8 NOT NULL,
  "mobile" varchar(255) COLLATE "pg_catalog"."default",
  "nickname" varchar(255) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "username" varchar(255) COLLATE "pg_catalog"."default",
  "last_modified_date" timestamptz(4),
  "enable" int4,
  "head_image_id" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS "t_user2";
CREATE TABLE "t_user2" (
  "create_date" timestamptz(4),
  "email" varchar(255) COLLATE "pg_catalog"."default",
  "id" int8 NOT NULL,
  "mobile" varchar(255) COLLATE "pg_catalog"."default",
  "nickname" varchar(255) COLLATE "pg_catalog"."default",
  "password" varchar(255) COLLATE "pg_catalog"."default",
  "username" varchar(255) COLLATE "pg_catalog"."default",
  "last_modified_date" timestamptz(4),
  "enable" int4,
  "head_image_id" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for t_user_app
-- ----------------------------
DROP TABLE IF EXISTS "t_user_app";
CREATE TABLE "t_user_app" (
  "user_id" int8 NOT NULL,
  "app_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Table structure for t_user_group
-- ----------------------------
DROP TABLE IF EXISTS "t_user_group";
CREATE TABLE "t_user_group" (
  "user_id" int8 NOT NULL,
  "group_id" int8 NOT NULL
)
;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS "t_user_role";
CREATE TABLE "t_user_role" (
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;

-- ----------------------------
-- Table structure for worker_node
-- ----------------------------
DROP TABLE IF EXISTS "worker_node";
CREATE TABLE "worker_node" (
  "id" int8 NOT NULL,
  "host_name" varchar(255) COLLATE "pg_catalog"."default",
  "port" varchar(255) COLLATE "pg_catalog"."default",
  "type" int4,
  "launch_date" timestamptz(4),
  "created" timestamptz(4),
  "modified" timestamptz(4)
)
;

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"seq_worker_node_id"', 1, true);

-- ----------------------------
-- Primary Key structure for table t_app
-- ----------------------------
ALTER TABLE "t_app" ADD CONSTRAINT "pk_app_id" PRIMARY KEY ("app_id");

-- ----------------------------
-- Primary Key structure for table t_group
-- ----------------------------
ALTER TABLE "t_group" ADD CONSTRAINT "pk_group_id" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_role
-- ----------------------------
ALTER TABLE "t_role" ADD CONSTRAINT "pk_role_id" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_user
-- ----------------------------
ALTER TABLE "t_user0" ADD CONSTRAINT "pk_user0_id" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_user
-- ----------------------------
ALTER TABLE "t_user1" ADD CONSTRAINT "pk_user1_id" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_user
-- ----------------------------
ALTER TABLE "t_user2" ADD CONSTRAINT "pk_user2_id" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_user_app
-- ----------------------------
ALTER TABLE "t_user_app" ADD CONSTRAINT "pk_user_app_id" PRIMARY KEY ("user_id", "app_id");

-- ----------------------------
-- Primary Key structure for table t_user_group
-- ----------------------------
ALTER TABLE "t_user_group" ADD CONSTRAINT "pk_user_group_id" PRIMARY KEY ("user_id", "group_id");

-- ----------------------------
-- Primary Key structure for table t_user_role
-- ----------------------------
ALTER TABLE "t_user_role" ADD CONSTRAINT "pk_user_role_id" PRIMARY KEY ("user_id", "role_id");

-- ----------------------------
-- Primary Key structure for table worker_node
-- ----------------------------
ALTER TABLE "worker_node" ADD CONSTRAINT "pk_worker_node_id" PRIMARY KEY ("id");
