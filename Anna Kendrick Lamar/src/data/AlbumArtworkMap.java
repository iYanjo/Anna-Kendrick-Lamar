package data;

public class AlbumArtworkMap {
    public static final String[] ALBUM_ARTWORK_EXTENSIONS = {".jpg", ".png", ".jpeg"}; //ordered by frequency
    public static final String[] ALBUM_ARTWORK_FILENAMES = {
            "My Beautiful Dark Twisted Fantasy.jpg",
            "Brothers.png",
            "How I Got Over.jpg",
            "There Is Love In You.jpg",
            "The Wild Hunt.jpg",
            "Plastic Beach.png",
            "Daughters.jpg",
            "This Is Happening.jpg",
            "The ArchAndroid.png",
            "Halcyon Digest.jpg",
            "High Violet.jpg",
            "The Age of Adz.jpg",
            "The Suburbs.jpg",
            "Cosmogramma.jpg",
            "Crystal Castles (II).jpg",
            "Sit Down, Man.jpg",
            "Before Today.png",
            "A Sufi and a Killer.jpg",
            "The Way Out.jpg",
            "Teen Dream.jpg",
            "Pilot Talk.jpg",
            "Fields.jpg",
            "The Monitor.jpg",
            "Feed-Forward.jpg",
            "Total Life Forever.jpg",
            "Pickin' Up the Pieces.jpg",
            "Xenoblade Chronicles.jpg",
            "NieR Gestalt & RepliCant.jpeg",
            "Return of 4Eva.jpg",
            "Undun.png",
            "Bon Iver.jpg",
            "Kaputt.jpg",
            "Strange Mercy.jpg",
            "Black Up.jpg",
            "An Empty Bliss Beyond This World.jpg",
            "Replica.jpg",
            "Twin Fantasy.jpg",
            "A I A Alien Observer.jpg",
            "Helplessness Blues.jpg",
            "Ravedeath, 1972.jpg",
            "Let England Shake.jpg",
            "XXX.jpg",
            "Instrumental Mixtape.jpg",
            "James Blake.jpg",
            "House of Balloons.jpg",
            "w h o k i l l.jpg",
            "Father, Son, Holy Ghost.jpg",
            "LiveLoveA$AP.png",
            "SBTRKT.jpg",
            "Gloss Drop.jpg",
            "Exmilitary.jpg",
            "Elmatic.png",
            "Far Side Virtual.jpg",
            "Pabst & Jazz.jpg",
            "I Wish My Brother Rob Was Here.jpg",
            "Covert Coup.jpg",
            "Give Me My Flowers While I Can Still Smell Them.jpg",
            "Azari & III.jpg",
            "Torches.jpg",
            "Take the Kids Off Broadway.jpg",
            "Minecraft Volume Alpha.jpg",
            "Shields.jpeg",
            "Attack on Memory.jpeg",
            "Visions.jpg",
            "The Idler Wheel.jpg",
            "Allelujah! Don't Bend! Ascend!.jpg",
            "Mista Thug Isolation.jpg",
            "Lonerism.jpg",
            "channel ORANGE.jpg",
            "The Seer.jpg",
            "The Money Store.jpg",
            "good kid, m.A.A.d city.jpg",
            "TNGHT.jpg",
            "Kaleidoscope Dream.jpeg",
            "Kindred.jpg",
            "History Will Absolve Me.jpg",
            "Put Your Back N 2 It.jpeg",
            "Ondatrópica.jpg",
            "1999.png",
            "God's Father.jpg",
            "Instrumental Mixtape 2.jpg",
            "Truant  Rough Sleeper.png",
            "Duality.jpg",
            "The Woods.jpg",
            "Rare Chandeliers.jpg",
            "Bish Bosch.jpg",
            "Black Is Beautiful.jpg",
            "Niewidzialna Nerka.jpg",
            "An Awesome Wave.png",
            "Break It Yourself.jpg",
            "Yeezus.jpg",
            "...Like Clockwork.jpg",
            "Virgins.jpg",
            "Excavation.jpg",
            "Double Cup.jpg",
            "Random Access Memories.jpg",
            "Wakin on a Pretty Daze.jpg",
            "Shaking the Habitual.png",
            "Psychic.jpg",
            "I See Seaweed.jpg",
            "Fetch.jpg",
            "YAYAYI.jpg",
            "Ceres & Calypso in the Deep Time.jpg",
            "m b v.jpg",
            "Loud City Song.jpg",
            "Run the Jewels.jpg",
            "R Plus Seven.jpg",
            "Modern Vampires of the City.jpg",
            "0.jpg",
            "Sunbather.jpg",
            "Immunity.jpg",
            "Overgrown.jpg",
            "Jai Paul.jpg",
            "The Electric Lady.jpg",
            "Acid Rap.jpg",
            "Settle.jpg",
            "Tape Two.jpg",
            "BetterOffDEAD.png",
            "Melophobia.jpg",
            "Race Music.jpg",
            "How to Stop Your Brain in An Accident.jpg",
            "The Night's Gambit.jpg",
            "Teethed Glory and Injury.jpg",
            "&&&&&.jpg",
            "Moon Hooch.jpg",
            "Obsidian.jpg",
            "Delusional Thomas.jpg",
            "Alternative Trap.jpg",
            "Yessir Whatever.jpg",
            "Polo Sporting Goods.jpg",
            "The Hands That Thieve.jpg",
            "The #SWOUP Serengeti.jpg",
            "Sunset Blood.jpg",
            "II.jpg",
            "Drone Logic.jpg",
            "Livity Sound.jpg",
            "The Greatest Generation.jpg",
            "Perils From the Sea.jpg",
            "Utopia.jpg",
            "They Want My Soul.jpg",
            "The Unnatural World.jpg",
            "Dark Comedy.jpg",
            "None of This Is Real.jpg",
            "Plowing Into the Field of Love.jpg",
            "Metamodern Sounds In Country Music.jpg",
            "Home, Like Noplace Is There.jpg",
            "Ruins.jpg",
            "Lost in the Dream.jpg",
            "Bury Me at Makeout Creek.jpg",
            "You're Dead!.jpg",
            "I'm In Your Mind Fuzz.jpg",
            "Benji.jpg",
            "Black Messiah.jpg",
            "Run the Jewels 2.jpg",
            "Cocaine Piñata.jpg",
            "To Be Kind.jpeg",
            "Tha Tour Part 1.jpg",
            "Hell Can Wait.png",
            "Are We There.jpg",
            "Faces.jpg",
            "It's Album Time.jpg",
            "The Water[s].jpg",
            "So It Goes.png",
            "Oxymoron.png",
            "Welcome to Fazoland.jpg",
            "Fuck Off Get Free We Pour Light On Everything.jpg",
            "New York Telephone.jpg",
            "Perfect Hair.jpg",
            "Pom Pom.jpg",
            "CLPPNG.jpg",
            "Days Before Rodeo.png",
            "Cilvia Demo.png",
            "III.jpg",
            "NEON iCON.jpg",
            "The God Complex.jpg",
            "thestand4rd.jpg",
            "Fugue State.jpg",
            "IVRY.jpg",
            "Tin Wooki.jpg",
            "Meshes of Voices.jpg",
            "Hot Dreams.jpg",
            "Black Metal.jpg",
            "When the World Was One.jpg",
            "Wendy.jpg",
            "Courting Strong.jpg",
            "Some Heavy Ocean.jpg",
            "Give the People What They Want.jpg",
            "Beyond the Black Rainbow.jpg",
            "Under the Skin.jpg",
            "Ping Pong the Animation.jpg",
            "LISA.jpg",
            "Sometimes I Sit and Think, and Sometimes I Just Sit.jpg",
            "Tetsuo & Youth.jpg",
            "Meliora.jpg",
            "We Cool.jpg",
            "So the Flies Don't Come.jpg",
            "I Love You, Honeybear.jpg",
            "In Colour.png",
            "Summertime '06.png",
            "NO NOW.jpeg",
            "Atarashī hi no tanjō.jpg",
            "Vulnicura.jpg",
            "Art Angels.jpg",
            "Abyss.jpg",
            "Garden of Delete.jpg",
            "Currents.png",
            "The Epic.jpg",
            "Exercises in Futility.jpg",
            "I Don't Like Shit I Don't Go Outside.png",
            "Rodeo.jpg",
            "E∙MO∙TION.jpg",
            "Have You In My Wilderness.jpg",
            "Divers.jpg",
            "Carrie & Lowell.jpg",
            "To Pimp A Butterfly.png",
            "If You're Reading This It's Too Late.jpg",
            "M3LL155X.jpg",
            "Every Hero Needs A Villain.jpg",
            "Get to Heaven.jpg",
            "DAOKO.jpg",
            "Sprained Ankle.jpg",
            "Soul Food.jpg",
            "Maxo 187.jpg",
            "Lil Me.jpg",
            "I Wish Shit Would Stop Spinning.jpg",
            "Third Side of Tape.jpg",
            "VEGA INTL. Night School.jpg",
            "The Woman at the End of the World.jpg",
            "Under the Red Cloud.jpg",
            "Junun.jpg",
            "Bloodborne The Old Hunters.jpg",
            "DOOM.jpg",
            "Honor Killed the Samurai.png",
            "Bonito Generation.jpg",
            "A Seat at the Table.jpg",
            "Plays the Music of Twin Peaks.jpg",
            "Malibu.jpg",
            "Imperial.png",
            "WORRY..png",
            "untitled unmastered..jpg",
            "Nonagon Infinity.jpg",
            "Terminal Redux.jpg",
            "Teens of Denial.jpg",
            "The Glowing Man.jpg",
            "Skeleton Tree.jpg",
            "Bottomless Pit.jpg",
            "We Got It From Here… Thank You 4 Your Service.jpg",
            "A Moon Shaped Pool.jpg",
            "Blonde.png",
            "Atrocity Exhibition.jpg",
            "Telefone.jpg",
            "My Woman.jpg",
            "Black Terry Cat.jpg",
            "Floss.jpg",
            "Vroom Vroom.jpg",
            "Still Brazy.jpg",
            "Awaken, My Love!.jpg",
            "The Persona Tape.jpg",
            "E∙MO∙TION Side B.png",
            "Castelos & ruínas.jpg",
            "Black Focus.jpg",
            "Wisdom of Elders.jpg",
            "Fly or Die.jpg",
            "The Visitor.jpg",
            "Marked for Death.jpg",
            "You Want It Darker.jpg",
            "May God Bless Your Hustle.jpg",
            "Process.jpg",
            "async.jpg",
            "No Shape.jpg",
            "444.png",
            "Neō Wax Bloom.png",
            "Forced Witness.jpg",
            "Brick Body Kids Still Daydream.jpg",
            "Brutalism.jpg",
            "Relatives In Descent.jpg",
            "All Bitches Die.jpg",
            "Melodrama.png",
            "Crack-Up.jpg",
            "SATURATION.jpg",
            "SATURATION II.jpg",
            "SATURATION III.png",
            "4eva Is a Mighty Long Time.png",
            "A Crow Looked at Me.jpg",
            "Flower Boy.jpg",
            "Drunk.png",
            "Aromanticism.jpg",
            "Bravado.jpeg",
            "Los Ángeles.jpg",
            "RINA.jpg",
            "Pure Comedy.jpg",
            "Common As Light and Love Are Red Valleys of Blood.jpg",
            "Without Warning.jpg",
            "Red Burns.jpg",
            "2008.jpg",
            "Watch My Back.jpg",
            "Mourn.jpg",
            "The Never Story.jpg",
            "West 1996 Pt. 2.jpg",
            "The Animal Spirits.jpeg",
            "Peasant.jpg",
            "Galaxies Like Grains of Sand.jpg",
            "Stranger in the Alps.jpg",
            "...Because I'm Young Arrogant and Hate Everything You Stand For.jpg",
            "Makin' Magick.jpg",
            "Superflat.jpg",
            "Sudan Archives.jpg",
            "Death to the Planet.jpg",
            "La Saboteuse.jpg",
            "L'Rain.jpg",
            "Guppy.jpg",
            "Soul of a Woman.jpg",
            "Bark Your Head Off, Dog.jpg",
            "Be the Cowboy.jpg",
            "Care For Me.png",
            "Musas Vol. 2.jpg",
            "El Mal Querer.jpg",
            "qp.jpg",
            "Avantdale Bowling Club.jpg",
            "Die Lit.jpg",
            "Isolation.jpg",
            "Room 25.jpg",
            "Your Queen Is A Reptile.jpg",
            "OIL OF EVERY PEARL'S UN-INSIDES.jpg",
            "Veteran.jpg",
            "Crumbling.jpg",
            "Wide Awake!.jpg",
            "Joy as an Act of Resistance.jpg",
            "Endless.jpg",
            "Little Dark Age.png",
            "Dead Magic.jpg",
            "2012-2017.png",
            "TA13OO.png",
            "Some Rap Songs.jpg",
            "DAYTONA.jpg",
            "KIDS SEE GHOSTS.jpg",
            "You Won't Get What You Want.jpg",
            "Paraffin.jpg",
            "harutosyura.jpg",
            "(american) FOOL.jpg",
            "Pastoral.jpg",
            "Streams of Thought Vol. 1.jpg",
            "City Morgue Vol 1 Hell Or High Water.jpg",
            "Con Todo El Mundo.jpg",
            "War In My Pen.jpg",
            "Everything's Fine.jpg",
            "Bread.jpg",
            "The Ugly Art.jpg",
            "Together Through Time.jpg",
            "Nothing 2 Loose.jpg",
            "Highlights from the Book Beri'ah.jpg",
            "Ensley.jpg",
            "Universal Beings.jpg",
            "By the Way, I Forgive You.jpg",
            "Temaukel, the Spirit Before Time.jpg",
            "Let It Wander.jpg",
            "Dans ma main.jpg",
            "Please Don't Be Dead.png",
            "The Sciences.jpg",
            "Death in Haiti.jpg",
            "Masana Temples.jpg",
            "Rhododendron.jpg",
            "Hiding Places.png",
            "Plastic Anniversary.jpg",
            "Guns.jpg",
            "Fyah.jpg",
            "GREY Area.jpg",
            "The Origin of My Depression.jpg",
            "A Quiet Farewell, 2016-2018.jpg",
            "Titanic Rising.jpg",
            "Schlagenheim.jpeg",
            "1000 Gecs.jpg",
            "ITEKOMA HITS.jpg",
            "Trust In the Lifeforce of the Deep Mystery.jpg",
            "Bandana.jpg",
            "IGOR.jpg",
            "Purple Mountains.jpg",
            "Caligula.jpg",
            "Little Electric Chicken Heart.jpg",
            "There Existed An Addiction to Blood.png",
            "MAGDALENE.jpg",
            "COIN COIN Chapter Four Memphis.jpg",
            "H.A.Q.Q..jpg",
            "Norman Fucking Rockwell!.jpg",
            "Orchestre univers.jpg",
            "Girl with Basket of Fruit.jpg",
            "All My Heroes Are Cornballs.png",
            "2020.png",
            "Charli.png",
            "Reckoning.jpg",
            "Burd.jpg",
            "Utility.jpg",
            "Bonsoir, Fucker.jpg",
            "Everywhere at the End of Time.jpg",
            "DEUCE.jpg",
            "WWCD.jpg",
            "Son of Serpentine.jpg",
            "Legacy! Legacy!.jpg",
            "The Return.jpg",
            "All Mirrors.jpg"};
}
