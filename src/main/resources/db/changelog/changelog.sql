-- liquibase formatted sql

-- changeset ТестоКРАБ:1692024924895-1
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'category';
CREATE TABLE "category" ("id" UUID DEFAULT uuid_generate_v4() NOT NULL, "name" VARCHAR, "parent_id" UUID, "imageurl" VARCHAR, CONSTRAINT "category_pkey" PRIMARY KEY ("id"));

-- changeset ТестоКРАБ:1692024924895-2
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'order';
CREATE TABLE "order" ("id" UUID DEFAULT uuid_generate_v4() NOT NULL, "address" VARCHAR, "user_id" UUID, "product_id" UUID, "quantity" INTEGER, "final_price" FLOAT8, "status" INTEGER, CONSTRAINT "order_pkey" PRIMARY KEY ("id"));

-- changeset ТестоКРАБ:1692024924895-3
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'product';
CREATE TABLE "product" ("id" UUID DEFAULT uuid_generate_v4() NOT NULL, "name" VARCHAR, "price" FLOAT8, "category_id" UUID, "vendor_id" UUID, "imageurl" VARCHAR, "description" VARCHAR, CONSTRAINT "product_pkey" PRIMARY KEY ("id"));

-- changeset ТестоКРАБ:1692024924895-4
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'user';
CREATE TABLE "user" ("id" UUID DEFAULT uuid_generate_v4() NOT NULL, "username" VARCHAR NOT NULL, "password" VARCHAR NOT NULL, "email" VARCHAR NOT NULL, "role" VARCHAR, CONSTRAINT "user_pkey" PRIMARY KEY ("id"));

-- changeset ТестоКРАБ:1692024924895-5
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'user_information';
CREATE TABLE "user_information" ("id" UUID DEFAULT uuid_generate_v4() NOT NULL, "user_id" UUID, "name" VARCHAR, "surname" VARCHAR, "phone" VARCHAR, "address" VARCHAR, CONSTRAINT "user_information_pkey" PRIMARY KEY ("id"));

-- changeset ТестоКРАБ:1692024924895-6
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'vendor';
CREATE TABLE "vendor" ("id" UUID DEFAULT uuid_generate_v4() NOT NULL, "name" VARCHAR, CONSTRAINT "vendor_pkey" PRIMARY KEY ("id"));

-- changeset ТестоКРАБ:1692024924895-7
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT parent_id FROM information_schema.columns WHERE table_name='category' and column_name='parent_id';
ALTER TABLE "category" ADD CONSTRAINT "category_parent_id_fkey" FOREIGN KEY ("parent_id") REFERENCES "category" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset ТестоКРАБ:1692024924895-8
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT category_id FROM information_schema.columns WHERE table_name='product' and column_name='category_id';
ALTER TABLE "product" ADD CONSTRAINT "product_category_id_fkey" FOREIGN KEY ("category_id") REFERENCES "category" ("id") ON UPDATE NO ACTION ON DELETE CASCADE;

-- changeset ТестоКРАБ:1692024924895-9
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT vendor_id FROM information_schema.columns WHERE table_name='product' and column_name='vendor_id';
ALTER TABLE "product" ADD CONSTRAINT "product_vendor_id_fkey" FOREIGN KEY ("vendor_id") REFERENCES "vendor" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset ТестоКРАБ:1692024924895-10
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT user_id FROM information_schema.columns WHERE table_name='user_information' and column_name='user_id';
ALTER TABLE "user_information" ADD CONSTRAINT "userinformation_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "user" ("id") ON UPDATE NO ACTION ON DELETE CASCADE;

-- changeset ТестоКРАБ:data-11
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT count(id) FROM "user" WHERE username = 'admin'
INSERT INTO "user" (USERNAME, PASSWORD, EMAIL, ROLE) VALUES ('admin', '$2a$10$cdlnPAaLR5Mmyhz7xBQ2vePO.OoE8OLC/w7vO/20trtWDMOrcmdh.', 'email@mail.ru', 'ADMIN');