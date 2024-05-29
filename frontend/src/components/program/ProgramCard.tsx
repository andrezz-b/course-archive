import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "../ui/card";
import { Button } from "../ui/button";
import { useMemo } from "react";
import { cn } from "@/lib/utils";
import { Link } from "react-router-dom";
import { Program } from "@/types/Program";
import { InfiniteCardProps } from "../InfiniteCardList";

const ProgramCard = ({ item: program }: InfiniteCardProps<Program>) => {
	const truncatedDescription = useMemo(() => {
		if (!program.description) return "";
		if (program.description.length > 90) {
			return program.description.slice(0, 90) + "...";
		}
		return program.description;
	}, [program.description]);

	return (
		<Card className="min-w-[300px] max-w-[400px] md:w-[325px] md:h-[365px] flex flex-col max-h-[400px]">
			<CardHeader className="flex flex-col justify-around h-[120px]">
				<CardTitle>{program.name}</CardTitle>
			</CardHeader>
			<CardContent className="flex-grow">
				<p className={cn({ "fade-out": truncatedDescription.endsWith("...") }, "overflow-hidden")}>
					{truncatedDescription}
				</p>
			</CardContent>
			<CardFooter className="flex justify-between flex-row-reverse">
				{/*<Link to={`./${program.id}`}>*/}
				{/*  <Button className="place-self-end">More info</Button>*/}
				{/*</Link>*/}
				<Link
					to={{
						pathname: "/course",
						search: `?programId=${program.id}`,
					}}
				>
					<Button className="place-self-end">View courses</Button>
				</Link>
			</CardFooter>
		</Card>
	);
};

export default ProgramCard;
