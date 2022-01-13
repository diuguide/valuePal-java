CREATE TRIGGER valuepaldev.updateavgpricesingleholding
    AFTER
        INSERT
        OR
UPDATE
    ON
    valuepaldev.holdings
    EXECUTE PROCEDURE valuepaldev.updateavgprice2();

CREATE OR REPLACE FUNCTION valuepaldev.calcavgprice(
	p_newprice valuepaldev.holdings.price%TYPE,
	p_wallet_id valuepaldev.holdings.wallet_id%TYPE,
	p_ticker valuepaldev.holdings.ticker%TYPE
)
RETURNS DOUBLE PRECISION
LANGUAGE plpgsql
AS $$
DECLARE
v_avg valuepaldev.holdings.avg_purchase_price%TYPE;
BEGIN
SELECT
    avg(price)
INTO
    v_avg
FROM
    valuepaldev.orders
WHERE
        ticker = p_ticker
  AND wallet_id = p_wallet_id;

RETURN (v_avg + p_newprice) / 2;
END;

$$

CREATE OR REPLACE FUNCTION valuepaldev.updateavgprice2()
RETURNS TRIGGER AS $updateavgpricesingleholding$
DECLARE
rec_count int;
BEGIN
SELECT
    count(*)
INTO
    rec_count
FROM
    valuepaldev.orders
WHERE
        ticker = NEW.ticker
  AND wallet_id = NEW.wallet_id;

IF(rec_count > 0) THEN
		NEW.avg_purchase_price := valuepaldev.calcavgprice(NEW.price,
NEW.wallet_id,
NEW.ticker);

RETURN NEW;
ELSE
		NEW.avg_purchase_price := NEW.price;

RETURN NEW;
END IF;
END
$valuepaldev.updateavgpricesingleholding$ LANGUAGE plpgsql;
