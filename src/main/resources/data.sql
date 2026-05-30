DELETE FROM users;
ALTER TABLE users AUTO_INCREMENT = 1;

DELETE FROM product;
ALTER TABLE product AUTO_INCREMENT = 1;

INSERT INTO product
(id, author, category, created_at, description, image, image_url, name, price, stock, updated_at)
VALUES
    (1, NULL, 'MAIN', NOW(6), '高たんぱくでヘルシーな定番チキンプレートです。', 'grilled-chicken-plate.jpg', NULL, 'グリルチキンプレート', 1000, 10, NOW(6)),
    (2, NULL, 'LOW CARB', NOW(6), '糖質を抑えたスパイス香るキーマカレーです。', 'lowcarb-keema-curry.jpg', NULL, '低糖質キーマカレー', 1200, 10, NOW(6)),
    (3, NULL, 'PROTEIN', NOW(6), 'たんぱく質をしっかり摂れるサラダボウルです。', 'protein-salad-bowl.jpg', NULL, '高たんぱくサラダボウル', 1000, 10, NOW(6)),
    (4, NULL, 'MAIN', NOW(6), 'まろやかな味わいのバターチキンカレーです。', 'butter-chicken-curry.jpg', NULL, 'バターチキンカレー', 1100, 10, NOW(6)),
    (5, NULL, 'SIDE DISH', NOW(6), '朝食や軽食に合うアサイーボウルです。', 'acai-bowl.jpg', NULL, 'アサイーボウル', 900, 10, NOW(6)),
    (6, NULL, 'DRINK', NOW(6), 'ベリーの酸味が爽やかなスムージーです。', 'berry-smoothie.jpg', NULL, 'ベリースムージー', 700, 10, NOW(6)),
    (7, NULL, 'PROTEIN', NOW(6), '鶏むね肉をハーブで香ばしく焼き上げました。', 'herb-chicken.jpg', NULL, '鶏むね肉のハーブ焼き', 1000, 10, NOW(6)),
    (8, NULL, 'MAIN', NOW(6), '鮭を使ったバランスのよいグリルプレートです。', 'salmon-grill-plate.jpg', NULL, '鮭のグリルプレート', 1200, 10, NOW(6));

ALTER TABLE product AUTO_INCREMENT = 9;

INSERT INTO users
(id, email, password, role, username)
VALUES
    (1, 'test@example.com', '$2a$10$2cC/WGoUNv.DlGb3AWvFVukbwdnBqB5RPcS9HPpl8Ai3mqsejBRlq', 'ROLE_USER', 'testuser'),
    (2, 'admin@example.com', '$2a$10$2cC/WGoUNv.DlGb3AWvFVukbwdnBqB5RPcS9HPpl8Ai3mqsejBRlq', 'ROLE_ADMIN', 'admin');

ALTER TABLE users AUTO_INCREMENT = 3;