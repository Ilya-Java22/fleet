UPDATE enterprises
SET timezone =
    CASE 
        WHEN id = 1 THEN 'Europe/Moscow'
        WHEN id = 2 THEN 'UTC'
        WHEN id = 3 THEN 'Asia/Tokyo'
    END;