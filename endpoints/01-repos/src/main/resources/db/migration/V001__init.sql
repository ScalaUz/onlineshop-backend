CREATE TYPE ROLE AS ENUM ('tech_admin', 'moderator', 'customer');

CREATE TABLE IF NOT EXISTS countries(
  id UUID PRIMARY KEY NOT NULL,
  name_uz VARCHAR NOT NULL,
  name_ru VARCHAR NOT NULL,
  name_en VARCHAR NOT NULL
);
-- Insert country Uzbekistan
INSERT INTO countries VALUES
  ('162b6536-5a98-4d42-87dd-931b21e8e396', 'O`zbekistan', 'Узбекистан', 'Uzbekistan');

CREATE TABLE IF NOT EXISTS regions(
  id UUID PRIMARY KEY NOT NULL,
  country_id UUID NOT NULL CONSTRAINT fk_countries REFERENCES countries (id) ON UPDATE CASCADE ON DELETE CASCADE,
  name_uz VARCHAR NOT NULL,
  name_ru VARCHAR NOT NULL,
  name_en VARCHAR NOT NULL
);
-- Insert regions for Uzbekistan
INSERT INTO regions
VALUES
  ('97ce712a-0304-4707-95e3-7870d9470ebb', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Tashkent', 'Тошкент', 'Tashkent'),
  ('9bec09b9-70e0-45c1-bf3d-bbaba221ad21', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Samarqand', 'Самарқанд', 'Samarkand'),
  ('51399a15-9839-4e3b-afa9-66e768bf482b', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Namangan', 'Наманган', 'Namangan'),
  ('b8e17080-5fb8-4795-bc2f-c89e12c29041', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Andijon', 'Андижон', 'Andijan'),
  ('326a49b2-98c7-4146-8ce6-f62746ac39f5', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Buxora', 'Бухоро', 'Bukhara'),
  ('7baa31f0-5145-4d6c-aeab-9419cd018097', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Qashqadaryo', 'Кашкадарья', 'Qashqadaryo'),
  ('dca1af75-5deb-44d8-a058-bcbc5a8fa85d', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Farg`ona', 'Фарғона', 'Fergana'),
  ('239b6a2c-914c-44a0-8b40-280f49529c6b', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Jizzax', 'Жиззах', 'Jizzakh'),
  ('7872e39c-391f-4cc5-9ef0-539974877fe5', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Sirdaryo', 'Сырдарья', 'Sirdaryo'),
  ('e1050f24-4ccd-4812-9abc-fe09ba0b0836', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Surxondaryo', 'Сурхандарья', 'Surkhandarya'),
  ('a6852ed4-4768-438a-9a5f-40a25546eca8', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Navoiy', 'Навоий', 'Navoiy'),
  ('3d9ecd13-4148-4354-bb3a-c51b1d533e51', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Xorazm', 'Хоразм', 'Khorezm'),
  ('b547d708-e175-4c4a-a526-2334cd6117f7', '162b6536-5a98-4d42-87dd-931b21e8e396', 'Qoraqalpogʻiston', 'Каракалпакстан', 'Karakalpakstan');

CREATE TABLE IF NOT EXISTS cities(
  id UUID PRIMARY KEY NOT NULL,
  region_id UUID NOT NULL CONSTRAINT fk_regions REFERENCES regions (id) ON UPDATE CASCADE ON DELETE CASCADE,
  name_uz VARCHAR NOT NULL,
  name_ru VARCHAR NOT NULL,
  name_en VARCHAR NOT NULL
);

-- Insert cities for the regions of Uzbekistan
-- Tashkent Region
INSERT INTO cities
VALUES
  ('154ebe1a-4c7a-433b-888c-27e1583adb00', '97ce712a-0304-4707-95e3-7870d9470ebb', 'Toshkent', 'Ташкент', 'Tashkent'),
  ('3db243e5-de46-4e3c-8421-97f5f10dbfc0', '97ce712a-0304-4707-95e3-7870d9470ebb', 'Yangiyo`l', 'Янгиёл', 'Yangiyul');

-- Samarqand Region
INSERT INTO cities
VALUES
  ('a021f741-708d-4f83-9782-8d09b7e76575', '9bec09b9-70e0-45c1-bf3d-bbaba221ad21', 'Samarqand', 'Самарканд', 'Samarkand'),
  ('d6076dac-3576-4a6a-b27c-4ccad5cc158a', '9bec09b9-70e0-45c1-bf3d-bbaba221ad21', 'Bulung`ur', 'Булунгур', 'Bulungur');

-- Qashqadaryo Region
INSERT INTO cities
VALUES
  ('921b2406-f217-4399-934f-9588977d4740', '7baa31f0-5145-4d6c-aeab-9419cd018097', 'Qarshi', 'Карши', 'Qarshi'),
  ('88fcece0-3c81-414b-ba83-184ff7755ceb', '7baa31f0-5145-4d6c-aeab-9419cd018097', 'Shahrisabz', 'Шахрисабз', 'Shahrisabz');

-- Namangan Region
INSERT INTO cities
VALUES
  ('f0d973db-7f2a-42d1-8eea-9df0b5f18357', '51399a15-9839-4e3b-afa9-66e768bf482b', 'Namangan', 'Наманган', 'Namangan'),
  ('527702b8-8724-43c8-bcd4-4f5d1d21eeff', '51399a15-9839-4e3b-afa9-66e768bf482b', 'Chust', 'Чуст', 'Chust');

-- Andijon Region
INSERT INTO cities
VALUES
  ('c7236200-1968-4498-b546-88eba8ccc478', 'b8e17080-5fb8-4795-bc2f-c89e12c29041', 'Andijon', 'Андижон', 'Andijan'),
  ('7f0d27d6-9135-43b1-99e1-2e9cfa843dfc', 'b8e17080-5fb8-4795-bc2f-c89e12c29041', 'Asaka', 'Асака', 'Asaka');

-- Bukhara Region
INSERT INTO cities
VALUES
  ('b163d505-adda-4e8a-aa40-2cadc1e69354', '326a49b2-98c7-4146-8ce6-f62746ac39f5', 'Bukhara', 'Бухоро', 'Bukhara'),
  ('2b19b80a-267b-486f-9dbb-0481d8c293cc', '326a49b2-98c7-4146-8ce6-f62746ac39f5', 'Kagan', 'Каган', 'Kagan');

-- Jizzakh Region
INSERT INTO cities
VALUES
  ('2af3880b-e374-46b5-9699-f71284fbaa2e', '239b6a2c-914c-44a0-8b40-280f49529c6b', 'Jizzakh', 'Жиззах', 'Jizzakh'),
  ('02e4e8fc-d516-4084-96dc-59480d33b491', '239b6a2c-914c-44a0-8b40-280f49529c6b', 'Gallaorol', 'Галлаорол', 'Gallaorol');

-- Fergana Region
INSERT INTO cities
VALUES
  ('a24b7960-763c-4991-8987-6fc0b6398c03', 'dca1af75-5deb-44d8-a058-bcbc5a8fa85d', 'Fergana', 'Фарғона', 'Fergana'),
  ('e82970b3-6b8c-4a34-8e70-2838ab728a9e', 'dca1af75-5deb-44d8-a058-bcbc5a8fa85d', 'Margilan', 'Маргилан', 'Margilan');

-- Nukus Region
INSERT INTO cities
VALUES
  ('c96252f2-d20e-445a-84fc-fe5813156d27', 'b547d708-e175-4c4a-a526-2334cd6117f7', 'Nukus', 'Нукус', 'Nukus'),
  ('f7519b6b-0694-4bce-aabf-120cd8612bdd', 'b547d708-e175-4c4a-a526-2334cd6117f7', 'Qonliko`l', 'Қонлиқўл', 'Konlikol');

-- Syrdarya Region
INSERT INTO cities
VALUES
  ('a43bc17c-d249-4069-be31-8f31411cfc1f', '7872e39c-391f-4cc5-9ef0-539974877fe5', 'Guliston', 'Гулистан', 'Guliston'),
  ('97d5cc17-9b34-4628-8ab5-7e859857652a', '7872e39c-391f-4cc5-9ef0-539974877fe5', 'Shirin', 'Ширин', 'Shirin');

-- Surkhandaryo Region
INSERT INTO cities
VALUES
  ('920605da-d66c-4a41-bce8-5d3ea6338bcf', 'e1050f24-4ccd-4812-9abc-fe09ba0b0836', 'Termez', 'Термез', 'Termez'),
  ('b337f207-d801-4752-8dec-2e165f33c271', 'e1050f24-4ccd-4812-9abc-fe09ba0b0836', 'Kumkurgan', 'Кумкурган', 'Kumkurgan');

-- Navoiy Region
INSERT INTO cities
VALUES
  ('8822b83f-afd6-4650-8e6d-575c08b21213', 'a6852ed4-4768-438a-9a5f-40a25546eca8', 'Navoiy', 'Навоий', 'Navoiy'),
  ('8abb95d2-223b-44b6-a523-0ffe48a5b9e1', 'a6852ed4-4768-438a-9a5f-40a25546eca8', 'Zarafshon', 'Зарафшон', 'Zarafshon');

-- Xorazm Region
INSERT INTO cities
VALUES
  ('a6e77914-ea06-4ef9-bc0e-8156f66a8ec0', '3d9ecd13-4148-4354-bb3a-c51b1d533e51', 'Urganch', 'Урганч', 'Urgench'),
  ('a0d6abfb-8233-4eb2-9686-66e49cc8b28a', '3d9ecd13-4148-4354-bb3a-c51b1d533e51', 'Xonobod', 'Хонабод', 'Khonobod');

CREATE TABLE IF NOT EXISTS assets(
  id UUID PRIMARY KEY NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  s3_key VARCHAR NOT NULL,
  public BOOLEAN NOT NULL,
  filename VARCHAR NULL
);

CREATE TABLE IF NOT EXISTS categories (
  id UUID PRIMARY KEY NOT NULL,
  name_uz VARCHAR NULL NULL,
  name_ru VARCHAR NULL NULL,
  name_en VARCHAR NULL NULL
);

CREATE TABLE IF NOT EXISTS brands (
  id UUID PRIMARY KEY NOT NULL,
  name VARCHAR NULL NULL,
  asset_id UUID NOT NULL CONSTRAINT fk_assets REFERENCES assets (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS products (
  id UUID PRIMARY KEY NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  name VARCHAR NULL NULL,
  linkCode VARCHAR NOT NULL UNIQUE,
  price NUMERIC NOT NULL,
  description_uz VARCHAR NULL,
  description_ru VARCHAR NULL,
  description_en VARCHAR NULL,
  brand_id UUID NOT NULL CONSTRAINT fk_brands REFERENCES brands (id) ON UPDATE CASCADE ON DELETE CASCADE,
  category_id UUID NOT NULL CONSTRAINT fk_categories REFERENCES categories (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users(
  id UUID PRIMARY KEY NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  firstname VARCHAR NOT NULL,
  lastname VARCHAR NOT NULL,
  role ROLE NOT NULL,
  phone VARCHAR NOT NULL UNIQUE,
  password VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS customers(
  id UUID PRIMARY KEY NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  firstname VARCHAR NOT NULL,
  lastname VARCHAR NOT NULL,
  phone VARCHAR NOT NULL UNIQUE,
  password VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS customers_addresses(
  customers_id UUID NOT NULL CONSTRAINT fk_users REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
  country_id UUID NOT NULL CONSTRAINT fk_countries REFERENCES countries (id) ON UPDATE CASCADE ON DELETE CASCADE,
  region_id UUID NOT NULL CONSTRAINT fk_regions REFERENCES regions (id) ON UPDATE CASCADE ON DELETE CASCADE,
  city_id UUID NOT NULL CONSTRAINT fk_cities REFERENCES cities (id) ON UPDATE CASCADE ON DELETE CASCADE,
  street VARCHAR NOT NULL,
  postal_code VARCHAR NOT NULL
);

INSERT INTO
  users
VALUES
  (
    '72a911c8-ad24-4e2d-8930-9c3ba51741df',
    '2023-06-30T16:02:51+05:00',
    'Maftunbek',
    'Raxmatov',
    'tech_admin',
    '+998999673398',
    '$s0$e0801$5JK3Ogs35C2h5htbXQoeEQ==$N7HgNieSnOajn1FuEB7l4PhC6puBSq+e1E8WUaSJcGY='
  );
