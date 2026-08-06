import Navbar from "../../components/common/Navbar";
import Hero from "../../pages/Landing/Hero";
import SearchSection from "../../pages/Landing/SearchSection";
import Categories from "../../pages/Landing/Categories";
import FeaturedTrips from "../../pages/Landing/FeaturedTrips";
import AIPlannerBanner from "../../pages/Landing/AIPlannerBanner";
import WhyChooseUs from "../../pages/Landing/WhyChooseUs";
import Testimonials from "../../pages/Landing/Testimonials";

import Footer from "../../components/common/Footer";

function LandingPage() {
  return (
    <>
      <Navbar />
      <Hero />
      <SearchSection />
      <Categories />
      <FeaturedTrips />
      <AIPlannerBanner />
      <WhyChooseUs />
      <Testimonials />
      <Footer />
    </>
  );
}

export default LandingPage;