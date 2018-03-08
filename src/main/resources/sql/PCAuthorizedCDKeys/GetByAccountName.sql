
SELECT pcacdk.AccountID ,
       pcacdk.IsAddingKey ,
       pcacdk.CDKey1 ,
       pcacdk.CDKey2 ,
       pcacdk.CDKey3 ,
       pcacdk.CDKey4 ,
       pcacdk.CDKey5 ,
       pcacdk.CDKey6 ,
       pcacdk.CDKey7 ,
       pcacdk.CDKey8 ,
       pcacdk.CDKey9 ,
       pcacdk.CDKey10
FROM dbo.PCAuthorizedCDKeys pcacdk
WHERE pcacdk.AccountID = :accountName