cd ./app/build/intermediates/classes/debug/
jar cvf hotfix_pack.jar  ./com/miqt/wand/ActivityDespacher.class ./com/example/motordex/AppParsenterImpl.class ./com/example/miqt/dexmvppdemo/MainActivity.class
dx --dex --output=../../../../../hotfix_pack.dex hotfix_pack.jar
