# gson-utils

Gson-utils makes it easier to serialize and deserialize certain immutable types
provided by Google Guava.

## Why / how?

Serializing and deserializing immutable value classes full of simple fields
is pretty straightforward with Gson, but it doesn't take much more complication
in contained data to suddenly demand more fiddliness to deal with.

Various other GitHub users have made projects to help with this, but each of the
ones I found in searches either had room for improvement, or a quirk that made
them less usable to me:
* It seems useless to serialize data if you can't expect to get the same data 
back out.  Several of the solutions I saw didn't guarantee the sort order of 
deserialized data would match the original.
* Maybe they didn't support a basic, important collection type
such as Set.
* Or maybe they didn't "deeply" serialize Collections of non-primitive objects.
* Or maybe a project I wanted to contribute to has its own particular restrictions
on dependencies or language features (reflection, injection, annotations, etc.)

If you do want to look into the projects I browsed through, they are:
* acebaggins/gson-serializers
* dampcake/gson-immutable
* rharter/gson-autovalue
* google/caliper's com.google.caliper.json package

Of these, given that we're talking about code meant to be used in conjunction 
with both Google Guava's collections and Google Gson, the package contained in
Google Caliper seemed like an appropriate starting point -- assuming it could
be made independent of that project (which is easy enough).  Meanwhile the rest
of the above other options are architected so differently that it still sounded
like a better idea to create a new project rather than contribute changes to
an existing one.

So this project starts with Caliper's code as a basis, then strips away some 
of that package's less essential-looking dependencies, resolves the sort order 
guarantees issue, and adds support for ImmutableSet.  And all the while the 
"meat" of it is trivially diff-able/comparable with "known-good" Google code.