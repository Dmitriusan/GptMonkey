# Extract items where piece count could not be determined
```sql
select *
from ebay_hawk_dev_db.merchandise_db.ebay_highlight eh
         inner join merchandise_db.ebay_finding ef
             on ef.id = eh.ebay_finding_id
where ef.number_of_pieces is NULL
```