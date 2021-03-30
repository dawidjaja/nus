# cs3211-project

## p1

## p2

## p3

Trace is generated from p2 CSP file `crawlerCSP_badSync_lightweight_v2`

Following are the specific trace that we have chosen:
1. First crawler fetch a url and write to BUL until it is full.
    - [EVENT 01] ct_selecturl.0.0 ->
    - [EVENT 02] ct_urlNotInIUT.0.0 ->
    - [EVENT 03] ct_addToIPW_success.0.0 ->
    - [EVENT 04] ct_writeToBUL_success.0.0 ->

2. Second Crawler fetches the same url and checks that it is not in the IUT hard disk yet.
Before it proceeds to start checking whether the url is in IPW, it starts to interleave.
This is the start of the data race.
Note that, if the second crawler does not interleave at this point, it would be able to detect that the url is in IPW.
Hence, it would avoid the data race.
    - [EVENT 05] ct_selectUrl.1.0 ->
    - [EVENT 06] ct_urlNotInIUT.1.0 ->
    - // INTERLEAVE HERE

3. IBT write all BUL content, which comes from first crawler, to IUT hard disk:
    - [EVENT 07] ibt_readFromBUL_success.0 ->
    - [EVENT 08] ibt_writeNext.0 ->
    - [EVENT 09] ibt_writeToIUT_success.0.0 ->

4. After writing them successfully to hard disk,
IBT updated IPW by removing the URL successfully from the IPW
    - [EVENT 10] ibt_removeFromIPW_success.0.0 ->

5. Unfortunately, the second crawler resumed the checking of IPW at this point
and managed to insert the duplicate URL to IPW.
Thus, data race has occurred.
    - [EVENT 11] ct_addToIPW_success.1.0 ->

6. Subsequently, the process goes on as usual and eventually writing a duplicate url.
    - [EVENT 12] ct_writeToBUL_success.1.0 ->
    - [EVENT 13] ibt_readFromBUL_success.1 ->
    - [EVENT 14] ibt_writeNext.1 ->
    - [EVENT 15] ibt_writeToIUT_success.1.0

For showing the data race, the program scale is different from p1. Following are the differences:
1. Number of URL = 1
2. Number of IBT = 2
3. Number of BUL = 2
4. BUL Size = 1
5. Number of CT per BUL = 1

## p4

