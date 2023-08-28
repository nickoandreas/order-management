START TRANSACTION;

INSERT INTO users
    (name, email, password, is_admin)
SELECT "Admin",
       "admin@gmail.com",
       "$2a$10$9Bj5yfRwha3p.Z5OUl4cZev01kNxwmTbH4FMPDmnqRBINdBC/mZra",
       1 WHERE NOT EXISTS (SELECT email FROM users WHERE email = "admin@gmail.com");

INSERT INTO products
    (sku, name, price, description, image)
SELECT "IDM-001",
       "Indomie Ayam Bawang",
       3500,
       "Mie Rebus Indomie Ayam Bawang",
       "indomie-ayam-bawang.jpg" WHERE NOT EXISTS (SELECT sku FROM products WHERE sku = "IDM-001");

INSERT INTO inventories
    (sku, qty_total, qty_reserved, qty_saleable)
SELECT p.sku, 100, 0, 100
FROM products p
WHERE p.sku = "IDM-001"
  AND NOT EXISTS (SELECT sku FROM inventories WHERE sku = p.sku);

INSERT INTO products
    (sku, name, price, description, image)
SELECT "IDM-002",
       "Indomie Ayam Panggang",
       3000,
       "Mie Goreng Indomie Ayam Panggang",
       "indomie-ayam-panggang.jpg" WHERE NOT EXISTS (SELECT sku FROM products WHERE sku = "IDM-002");

INSERT INTO inventories
    (sku, qty_total, qty_reserved, qty_saleable)
SELECT p.sku, 100, 0, 100
FROM products p
WHERE p.sku = "IDM-002" AND NOT EXISTS (SELECT sku FROM inventories WHERE sku = p.sku);

INSERT INTO products
    (sku, name, price, description, image)
SELECT "IDM-003",
       "Indomie Jumbo",
       5000,
       "Mie Goreng Indomie Jumbo",
       "indomie-jumbo.png" WHERE NOT EXISTS (SELECT sku FROM products WHERE sku = "IDM-003");

INSERT INTO inventories
    (sku, qty_total, qty_reserved, qty_saleable)
SELECT p.sku, 100, 0, 100
FROM products p
WHERE p.sku = "IDM-003"
  AND NOT EXISTS(SELECT sku FROM inventories WHERE sku = p.sku);

INSERT INTO products
    (sku, name, price, description, image)
SELECT "IDM-004",
       "Indomie Kebab Rendang",
       4000,
       "Mie Goreng Kebab Rendang",
       "indomie-kebab-rendang.jpg" WHERE NOT EXISTS (SELECT sku FROM products WHERE sku = "IDM-004");

INSERT INTO inventories
    (sku, qty_total, qty_reserved, qty_saleable)
SELECT p.sku, 100, 0, 100
FROM products p
WHERE p.sku = "IDM-004"
  AND NOT EXISTS(SELECT sku FROM inventories WHERE sku = p.sku);

INSERT INTO products
    (sku, name, price, description, image)
SELECT "IDM-005",
       "Indomie Matcha",
       4000,
       "Mie Goreng Matcha",
       "indomie-matcha.jpg" WHERE NOT EXISTS (SELECT sku FROM products WHERE sku = "IDM-005");

INSERT INTO inventories
    (sku, qty_total, qty_reserved, qty_saleable)
SELECT p.sku, 100, 0, 100
FROM products p
WHERE p.sku = "IDM-005"
  AND NOT EXISTS(SELECT sku FROM inventories WHERE sku = p.sku);

INSERT INTO products
    (sku, name, price, description, image)
SELECT "IDM-006",
       "Indomie Original",
       3000,
       "Mie Goreng Original",
       "indomie-original.jpg" WHERE NOT EXISTS (SELECT sku FROM products WHERE sku = "IDM-006");

INSERT INTO inventories
    (sku, qty_total, qty_reserved, qty_saleable)
SELECT p.sku, 100, 0, 100
FROM products p
WHERE p.sku = "IDM-006"
  AND NOT EXISTS(SELECT sku FROM inventories WHERE sku = p.sku);

COMMIT;

