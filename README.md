## DOCUMENTATION

## PAWEŁ ZĄBKIEWICZ

## DESCRIPTION:

### This is documentation for the BasketSplitter library, which includes unit tests.

#### Constructor:

in the constructor, the absolute path to the configuration file must be provided as an
argument. There is handle of IOException | ParseException e) and informs about a
possible error.

#### readMapFromFile method:

The function responsible for converting data from the json file to map<String,
List<String>> is the readMapFromFile function, which takes the previously provided
file path as an argument.
This function uses the json-simple:1.1.1 library
in this function I return a map in which the key is the product and the value is the list
of delivery options.

#### Split method:

This method is the main method of the library, which returns the final result.
it calls the 'makeReverseMap' function and then the 'MinimazeDeliveryOptions'
function.

#### makeReverseMap method:

In this method, the first map is create that include every item in the shopping cart, in
which the key is the item and the value is a list of delivery methods available for this
item. Then it turn the map over and finally get a map in which the key is the method
of delivery and the value is the items included in this delivery.
This function will pre-prepare the map for the resulting map.

#### sortReversedMapByItemsCountDesc method:

This function takes a previously prepared map as an argument. The purpose of this
method is to sort the map so that the delivery methods with the largest number of
items are at the beginning.

#### minimazeDeliveryOptions method:

This function takes a previously prepared map as an argument. This method is used
to minimize the number of delivery options in the context of a product list. Next, the
algorithm goes through each position in the map, starting from the position with the
largest number of products. A map is created in which the key is the delivery
method, and a list is created as a value to which the product is added. Thanks to the
set, the uniqueness of the products is ensured. The map that is returned is the result.

#### I built the package using Maven

#### I did tests for method:
'makeReverseMap','sortReversedMapByItemCountDesc', 'split'.
