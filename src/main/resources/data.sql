MERGE INTO roles AS target
USING (SELECT 'ROLE_ADMIN') AS source
ON (target.name = name)
WHEN NOT MATCHED THEN
    INSERT (name)
    VALUES ('ROLE_ADMIN');

MERGE INTO roles AS target
USING (SELECT 'ROLE_USER') AS source
ON (target.name = name)
WHEN NOT MATCHED THEN
    INSERT (name)
    VALUES ('ROLE_ADMIN');