CREATE OR REPLACE FUNCTION valuepaltest.updateavgprice1() RETURNS int
LANGUAGE plpgsql
AS $$
DECLARE
holding record;

num int;
BEGIN
	FOR holding IN (
	SELECT
		*
	FROM
		valuepaltest.holdings) loop
        UPDATE
	valuepaltest.holdings
SET
	avg_purchase_price =
        (
	SELECT
		avg(price)
	FROM
		valuepaltest.orders
	WHERE
		ticker = holding.ticker
		AND wallet_id = holding.wallet_id)
WHERE
	ticker = holding.ticker
	AND wallet_id = holding.wallet_id;

num = 1;

RETURN num;
END loop;
END;

$$

CREATE TRIGGER updateavgpricesingleholding
    AFTER
INSERT
	OR
UPDATE
	ON
	valuepaltest.holdings
                        EXECUTE PROCEDURE valuepaltest.updateavgprice2();

CREATE OR REPLACE FUNCTION valuepaltest.updateavgprice2()
RETURNS TRIGGER AS $updateavgpricesingleholding$
DECLARE
avg_price NUMERIC;
BEGIN
	SELECT
	avg(price)
INTO
	avg_price
FROM
	valuepaltest.orders
WHERE
	ticker = NEW.ticker
	AND wallet_id = NEW.wallet_id;

RETURN NEW;
END
$updateavgpricesingleholding$ LANGUAGE plpgsql;

DROP TRIGGER updateavgpricesingleholding ON
valuepaltest.holdings;

CREATE TRIGGER updateavgpricesingleholding
    BEFORE
INSERT
	OR
UPDATE
	ON
	valuepaltest.holdings
                         FOR EACH ROW
                         EXECUTE PROCEDURE valuepaltest.updateavgprice2();

CREATE OR REPLACE FUNCTION valuepaltest.calcavgprice(
	p_newprice valuepaltest.holdings.price%TYPE,
	p_wallet_id valuepaltest.holdings.wallet_id%TYPE,
	p_ticker valuepaltest.holdings.ticker%TYPE
)
RETURNS DOUBLE PRECISION
LANGUAGE plpgsql
AS $$
DECLARE
v_avg valuepaltest.holdings.avg_purchase_price%TYPE;
BEGIN
	SELECT
	avg(price)
INTO
	v_avg
FROM
	valuepaltest.orders
WHERE
	ticker = p_ticker
	AND wallet_id = p_wallet_id;

RETURN (v_avg + p_newprice) / 2;
END;

$$

CREATE OR REPLACE FUNCTION valuepaltest.updateavgprice2()
RETURNS TRIGGER AS $updateavgpricesingleholding$
DECLARE
rec_count int;
BEGIN
	SELECT
	count(*)
INTO
	rec_count
FROM
	valuepaltest.orders
WHERE
	ticker = NEW.ticker
	AND wallet_id = NEW.wallet_id;

IF(rec_count > 0) THEN
		NEW.avg_purchase_price := valuepaltest.calcavgprice(NEW.price,
NEW.wallet_id,
NEW.ticker);

RETURN NEW;
ELSE
		NEW.avg_purchase_price := NEW.price;

RETURN NEW;
END IF;
END
$updateavgpricesingleholding$ LANGUAGE plpgsql;
	