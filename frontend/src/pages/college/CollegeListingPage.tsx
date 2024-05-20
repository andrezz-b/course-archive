import CollegeCard from "@/components/CollegeCard";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { College } from "@/types/College";

const colleges: Array<College> = [
  {
    id: 1,
    name: "Tehnički Fakultet",
    acronym: "RITEH",
    city: "Rijeka",
    postcode: 51000,
    address: "Vukovarska 58",
    website: "http://www.riteh.uniri.hr/",
    description:
      "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Sed nobis facere numquam deserunt explicabo a ullam commodi autem fugit. Earum quos ab molestias?",
  },
  {
    id: 2,
    name: "Long College Name Very 2",
    acronym: "C2",
    city: "Realy Long City 2",
    postcode: 5678,
    address: "Address Is also Really Long 2",
    website: "Website 2",
    description:
      "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Sed nobis facere numquam deserunt explicabo a ullam commodi autem fugit. Earum quos ab molestias?",
  },
  {
    id: 3,
    name: "College 3",
    acronym: "C3",
    city: "City 3",
    postcode: 91011,
    address: "Address 3",
    website: "Website 3",
    description: "Description 3",
  },
  {
    id: 4,
    name: "Tehnički Fakultet",
    acronym: "RITEH",
    city: "Rijeka",
    postcode: 51000,
    address: "Vukovarska 58",
    website: "http://www.riteh.uniri.hr/",
    description:
      "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Sed nobis facere numquam deserunt explicabo a ullam commodi autem fugit. Earum quos ab molestias?",
  },
  {
    id: 22,
    name: "Long College Name Very 2",
    acronym: "C2",
    city: "Realy Long City 2",
    postcode: 5678,
    address: "Address Is also Really Long 2",
    website: "Website 2",
    description:
      "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Sed nobis facere numquam deserunt explicabo a ullam commodi autem fugit. Earum quos ab molestias?",
  },
  {
    id: 32,
    name: "College 3",
    acronym: "C3",
    city: "City 3",
    postcode: 91011,
    address: "Address 3",
    website: "Website 3",
    description: "Description 3",
  },
  {
    id: 11,
    name: "Tehnički Fakultet",
    acronym: "RITEH",
    city: "Rijeka",
    postcode: 51000,
    address: "Vukovarska 58",
    website: "http://www.riteh.uniri.hr/",
    description:
      "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Sed nobis facere numquam deserunt explicabo a ullam commodi autem fugit. Earum quos ab molestias?",
  },
  {
    id: 21,
    name: "Long College Name Very 2",
    acronym: "C2",
    city: "Realy Long City 2",
    postcode: 5678,
    address: "Address Is also Really Long 2",
    website: "Website 2",
    description:
      "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Sed nobis facere numquam deserunt explicabo a ullam commodi autem fugit. Earum quos ab molestias?",
  },
  {
    id: 31,
    name: "College 3",
    acronym: "C3",
    city: "City 3",
    postcode: 91011,
    address: "Address 3",
    website: "Website 3",
    description: "Description 3",
  },
];

const CollegeListingPage = () => {
  return (
    <div className="flex flex-col justify-center items-center mt-4 pb-4">
      <div className="max-w-[1200px] space-y-4 px-4 md:p-0">
        <h2>Browse Colleges</h2>
        <div className="flex flex-col md:flex-row items-start justify-between gap-2">
          <div className="flex">
            <Select defaultValue="name">
              <SelectTrigger className="w-[160px]">
                <SelectValue placeholder="" />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  <SelectItem value="name">Name</SelectItem>
                  <SelectItem value="city">City</SelectItem>
                  <SelectItem value="postcode">Postcode</SelectItem>
                </SelectGroup>
              </SelectContent>
            </Select>
            <Input placeholder="Search colleges" />
          </div>
          <Select defaultValue="name-asc">
            <SelectTrigger className="w-[200px]">
              Sort by:
              <SelectValue placeholder="" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="name-asc">Name: A to Z</SelectItem>
                <SelectItem value="name-desc">Name: Z to A</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4 w-full">
          {colleges.map((college) => (
            <CollegeCard key={college.id} college={college} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default CollegeListingPage;
