awk -F: '{print $1; print $2}' < data/ads.txt | db_load -T -t hash data/ad.idx;
awk -F: '{print $1; print $2}' < data/pdates.txt | db_load -T -t btree data/da.idx;
awk -F: '{print $1; print $2}' < data/prices.txt | db_load -T -t btree data/pr.idx;
awk -F: '{print $1; print $2}' < data/terms.txt | db_load -T -t btree data/te.idx;
