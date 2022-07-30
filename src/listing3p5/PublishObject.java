package listing3p5;

import dummy.Secret;

import java.util.HashSet;
import java.util.Set;

public class PublishObject {
    public static Set<Secret> knownSecrets;

    public void initialize() {
        knownSecrets = new HashSet<Secret>();
    }
}

/*
The most blatant form of publication is to store a reference in a public static field,
where any class and thread could see it, as in Listing 3.5

The initialize method instantiates a new HashSet and publishes it by storing a reference to it into knownSecrets.

Publishing one object may indirectly publish others.
If you add a Secret to the published knownSecrets set, you’ve also published that Secret,
because any code can iterate the Set and obtain a reference to the new Secret.
 */
