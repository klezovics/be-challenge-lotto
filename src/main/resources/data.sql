-- Not exists check added, so if its necessary to restart the app, there is no-need to clean the database

-- Insert tenants if not exists
INSERT INTO tenant (id, name)
SELECT 'a3e1b4c0-37b2-4c6f-a232-3f0f8e637e39', 'Tenant 1'
WHERE NOT EXISTS (SELECT 1 FROM tenant WHERE id = 'a3e1b4c0-37b2-4c6f-a232-3f0f8e637e39');

INSERT INTO tenant (id, name)
SELECT 'b7c9d6a2-8b4a-4f1e-839e-3c2f5b7a9623', 'Tenant 2'
WHERE NOT EXISTS (SELECT 1 FROM tenant WHERE id = 'b7c9d6a2-8b4a-4f1e-839e-3c2f5b7a9623');

-- Insert customers if not exists
INSERT INTO customer (id, number, tenant_id)
SELECT '9d21f1c8-9e5b-4d1d-a3bb-7f5e839b6f9c', 1, 'a3e1b4c0-37b2-4c6f-a232-3f0f8e637e39'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = '9d21f1c8-9e5b-4d1d-a3bb-7f5e839b6f9c');

INSERT INTO customer (id, number, tenant_id)
SELECT 'd4c9e2a8-4f3d-4d2e-b4b8-8f3a5c6e7b3d', 2, 'b7c9d6a2-8b4a-4f1e-839e-3c2f5b7a9623'
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE id = 'd4c9e2a8-4f3d-4d2e-b4b8-8f3a5c6e7b3d');

-- Insert balances if not exists
INSERT INTO balance (id, customer_id, balance_cents)
SELECT '5e3a2b1c-8c4d-4a2e-b3b7-4f2d5c6e8b1a', '9d21f1c8-9e5b-4d1d-a3bb-7f5e839b6f9c', 10000
WHERE NOT EXISTS (SELECT 1 FROM balance WHERE id = '5e3a2b1c-8c4d-4a2e-b3b7-4f2d5c6e8b1a');

INSERT INTO balance (id, customer_id, balance_cents)
SELECT '6f4b3d2a-8d5e-4b3e-c4c8-5f3a6d7e9b2b', 'd4c9e2a8-4f3d-4d2e-b4b8-8f3a5c6e7b3d', 10000
WHERE NOT EXISTS (SELECT 1 FROM balance WHERE id = '6f4b3d2a-8d5e-4b3e-c4c8-5f3a6d7e9b2b');
