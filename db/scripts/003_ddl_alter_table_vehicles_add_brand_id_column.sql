 ALTER TABLE vehicles ADD COLUMN if not exists brand_id int references brands(id);